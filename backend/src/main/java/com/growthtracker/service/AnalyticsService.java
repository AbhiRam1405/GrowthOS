package com.growthtracker.service;

import com.growthtracker.dto.WeeklyAnalyticsDTO;
import com.growthtracker.model.DailySummary;
import com.growthtracker.model.Task;
import com.growthtracker.model.TaskStatus;
import com.growthtracker.repository.DailySummaryRepository;
import com.growthtracker.repository.TaskRepository;
import com.growthtracker.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Computes weekly analytics for the last 7 days.
 *
 * KEY BEHAVIOR:
 * - Weekly frequency tasks are EXCLUDED from daily streak calculations
 *   but ARE included in weekly analytics (weakest/strongest task logic).
 * - longestStreak is read from persisted DB records.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final DailySummaryRepository dailySummaryRepository;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public WeeklyAnalyticsDTO getWeeklyAnalytics() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6);

        String fromStr = sevenDaysAgo.format(DATE_FMT);
        String toStr = today.format(DATE_FMT);

        // Summaries for last 7 days (for average and chart)
        List<DailySummary> summaries = dailySummaryRepository
            .findByDateBetweenOrderByDateAsc(fromStr, toStr);

        // Build daily progress list — fill in 0 for missing days
        List<WeeklyAnalyticsDTO.DailyProgress> dailyProgress = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            String dayStr = day.format(DATE_FMT);
            double pct = summaries.stream()
                .filter(s -> s.getDate().equals(dayStr))
                .mapToDouble(DailySummary::getCompletionPercentage)
                .findFirst()
                .orElse(0.0);
            dailyProgress.add(new WeeklyAnalyticsDTO.DailyProgress(dayStr, pct));
        }

        double weeklyAverage = dailyProgress.stream()
            .mapToDouble(WeeklyAnalyticsDTO.DailyProgress::getCompletionPercentage)
            .average()
            .orElse(0.0);

        // Current streak from today's summary (or most recent available)
        int currentStreak = summaries.stream()
            .filter(s -> s.getDate().equals(toStr))
            .mapToInt(DailySummary::getStreak)
            .findFirst()
            .orElse(0);

        // Longest streak from all DB records (not just last 7)
        int longestStreak = dailySummaryRepository.findAllByOrderByDateDesc().stream()
            .mapToInt(DailySummary::getLongestStreak)
            .max()
            .orElse(0);

        // Task completion frequency for last 7 days (ALL tasks — Daily + Weekly)
        List<Task> allTasks = taskRepository.findAll();
        List<TaskStatus> weekStatuses = taskStatusRepository
            .findByDateBetween(sevenDaysAgo, today);

        // Count completions per task
        Map<String, Long> completionCounts = weekStatuses.stream()
            .filter(TaskStatus::isCompleted)
            .collect(Collectors.groupingBy(TaskStatus::getTaskId, Collectors.counting()));

        String weakestTask = "N/A";
        String strongestTask = "N/A";

        if (!allTasks.isEmpty()) {
            // Strongest: most completions
            strongestTask = allTasks.stream()
                .max(Comparator.comparingLong(t -> completionCounts.getOrDefault(t.getId(), 0L)))
                .map(Task::getTitle)
                .orElse("N/A");

            // Weakest: fewest completions (could be 0)
            weakestTask = allTasks.stream()
                .min(Comparator.comparingLong(t -> completionCounts.getOrDefault(t.getId(), 0L)))
                .map(Task::getTitle)
                .orElse("N/A");
        }

        return WeeklyAnalyticsDTO.builder()
            .weeklyAverage(Math.round(weeklyAverage * 100.0) / 100.0)
            .currentStreak(currentStreak)
            .longestStreak(longestStreak)
            .weakestTask(weakestTask)
            .strongestTask(strongestTask)
            .dailyProgress(dailyProgress)
            .build();
    }

    /**
     * Returns completion counts per category over last 7 days.
     * Example: { "Health": 5, "Coding": 3, "Interview": 2 }
     */
    public Map<String, Long> getCategoryAnalytics() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6);

        List<Task> allTasks = taskRepository.findAll();
        List<TaskStatus> weekStatuses = taskStatusRepository
            .findByDateBetween(sevenDaysAgo, today);

        // Build taskId → category map
        Map<String, String> taskCategoryMap = allTasks.stream()
            .collect(Collectors.toMap(Task::getId, Task::getCategory));

        // Count completed statuses per category
        return weekStatuses.stream()
            .filter(TaskStatus::isCompleted)
            .filter(s -> taskCategoryMap.containsKey(s.getTaskId()))
            .collect(Collectors.groupingBy(
                s -> taskCategoryMap.get(s.getTaskId()),
                Collectors.counting()
            ));
    }
}
