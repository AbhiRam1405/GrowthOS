package com.growthtracker.service;

import com.growthtracker.dto.TaskWithStatusDTO;
import com.growthtracker.model.Task;
import com.growthtracker.model.TaskStatus;
import com.growthtracker.repository.TaskRepository;
import com.growthtracker.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manages task completion status for a given date.
 * Upserts (insert or update) a TaskStatus record, then triggers daily summary recalculation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final DailySummaryService dailySummaryService;

    /**
     * Mark or toggle a task's completion status for a given date.
     * If a record already exists → flip the completed flag.
     * If not → insert with completed = true.
     * Then recalculate the daily summary.
     */
    public TaskStatus markStatus(String taskId, LocalDate date, boolean completed) {
        Optional<TaskStatus> existing = taskStatusRepository.findByTaskIdAndDate(taskId, date);

        TaskStatus status;
        if (existing.isPresent()) {
            status = existing.get();
            status.setCompleted(completed);
        } else {
            status = TaskStatus.builder()
                .taskId(taskId)
                .date(date)
                .completed(completed)
                .build();
        }

        TaskStatus saved = taskStatusRepository.save(status);
        log.info("Marked task {} as {} on {}", taskId, completed, date);

        // Recalculate daily summary after status change
        dailySummaryService.recompute(date);

        return saved;
    }

    public List<TaskWithStatusDTO> getTasksWithStatus(LocalDate date) {
        List<Task> allTasks = taskRepository.findAll();
        
        // Filter tasks: Daily and Weekly appear every day. One-time tasks only on their scheduled date.
        List<Task> filteredTasks = allTasks.stream()
            .filter(t -> !"One-time".equalsIgnoreCase(t.getFrequency()) || date.equals(t.getScheduledDate()))
            .toList();

        List<TaskStatus> statuses = taskStatusRepository.findByDate(date);

        // Build lookup map: taskId → completed
        Map<String, Boolean> statusMap = statuses.stream()
            .collect(Collectors.toMap(TaskStatus::getTaskId, TaskStatus::isCompleted));

        return filteredTasks.stream()
            .map(task -> TaskWithStatusDTO.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .category(task.getCategory())
                .frequency(task.getFrequency())
                .scheduledDate(task.getScheduledDate())
                .completed(statusMap.getOrDefault(task.getId(), false))
                .build())
            .toList();
    }
}
