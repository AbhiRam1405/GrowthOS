package com.growthtracker.service;

import com.growthtracker.model.Task;
import com.growthtracker.model.TaskStatus;
import com.growthtracker.repository.DailySummaryRepository;
import com.growthtracker.repository.TaskRepository;
import com.growthtracker.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Rule-based AI suggestion engine.
 *
 * Rules (checked in priority order):
 *  1. No tasks exist → onboarding message
 *  2. Gym missed 3 consecutive days → health consistency message
 *  3. Interview Practice completed < 2 times last 7 days → readiness message
 *  4. weeklyAverage < 50 → performance message
 *  5. streak == 0 (broken) → momentum message
 *  6. streak >= 7 → praise message
 *  7. Default motivational message
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final DailySummaryRepository dailySummaryRepository;

    public String getSuggestion() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6);

        List<Task> allTasks = taskRepository.findAll();

        // Rule 1: No tasks
        if (allTasks.isEmpty()) {
            return "🚀 Start by adding your first task! Consistent tracking is the first step to growth.";
        }

        List<TaskStatus> weekStatuses = taskStatusRepository.findByDateBetween(sevenDaysAgo, today);

        // Build taskId → title map for rule matching
        Map<String, String> taskTitleMap = allTasks.stream()
            .collect(Collectors.toMap(Task::getId, Task::getTitle));

        // Rule 2: Gym missed 3 consecutive days
        Optional<Task> gymTask = allTasks.stream()
            .filter(t -> t.getTitle().toLowerCase().contains("gym")
                || t.getCategory().toLowerCase().contains("health"))
            .findFirst();

        if (gymTask.isPresent()) {
            boolean missedThree = isTaskMissedConsecutiveDays(gymTask.get().getId(), weekStatuses, today, 3);
            if (missedThree) {
                return "🏋️ Your health routine needs consistency. You've missed Gym/Health for 3 consecutive days — get back on track!";
            }
        }

        // Rule 3: Interview Practice < 2 times last 7 days
        Optional<Task> interviewTask = allTasks.stream()
            .filter(t -> t.getTitle().toLowerCase().contains("interview")
                || t.getCategory().toLowerCase().contains("interview"))
            .findFirst();

        if (interviewTask.isPresent()) {
            long interviewCompletions = weekStatuses.stream()
                .filter(s -> s.getTaskId().equals(interviewTask.get().getId()) && s.isCompleted())
                .count();
            if (interviewCompletions < 2) {
                return "📋 Increase interview preparation to improve placement readiness. Aim for at least 3 sessions per week!";
            }
        }

        // Rule 4: Weekly average < 50%
        double weeklyAvg = dailySummaryRepository
            .findByDateBetweenOrderByDateAsc(sevenDaysAgo.toString(), today.toString())
            .stream()
            .mapToDouble(s -> s.getCompletionPercentage())
            .average()
            .orElse(0.0);

        if (weeklyAvg < 50.0) {
            return "📉 Your performance is below your potential. Focus on completing at least 70% of your tasks daily to build momentum!";
        }

        // Rule 5: Streak broken (today's streak is 0 but yesterday's was > 0)
        int todayStreak = dailySummaryRepository.findById(today.toString())
            .map(s -> s.getStreak()).orElse(-1);
        int yesterdayStreak = dailySummaryRepository.findById(today.minusDays(1).toString())
            .map(s -> s.getStreak()).orElse(0);

        if (todayStreak == 0 && yesterdayStreak > 0) {
            return "💫 Don't let one bad day stop your momentum. Your streak was broken, but you can start a new one today!";
        }

        // Rule 6: Long active streak — praise
        if (todayStreak >= 7) {
            return "🔥 Incredible! You've maintained a " + todayStreak + "-day streak! Keep pushing — consistency is your superpower!";
        }

        // Rule 7: Default
        return "✅ You're making progress! Keep completing your tasks daily to build an unbreakable streak. You've got this!";
    }

    /**
     * Checks if a task was NOT completed for the last `days` consecutive days.
     */
    private boolean isTaskMissedConsecutiveDays(String taskId, List<TaskStatus> statuses,
                                                 LocalDate upTo, int days) {
        Set<LocalDate> completedDates = statuses.stream()
            .filter(s -> s.getTaskId().equals(taskId) && s.isCompleted())
            .map(TaskStatus::getDate)
            .collect(Collectors.toSet());

        for (int i = 1; i <= days; i++) {
            LocalDate day = upTo.minusDays(i);
            if (completedDates.contains(day)) {
                return false; // was completed that day, not missed
            }
        }
        return true;
    }
}
