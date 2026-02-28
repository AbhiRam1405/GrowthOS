package com.growthtracker.service;

import com.growthtracker.dto.TaskDTO;
import com.growthtracker.exception.DuplicateTitleException;
import com.growthtracker.exception.ResourceNotFoundException;
import com.growthtracker.model.Task;
import com.growthtracker.repository.TaskRepository;
import com.growthtracker.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Task CRUD operations.
 * Enforces unique title constraint at the service layer (complementing the DB index).
 * Cascades deletes to TaskStatus records to avoid orphaned data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public Task createTask(TaskDTO dto) {
        if (taskRepository.existsByTitle(dto.getTitle())) {
            throw new DuplicateTitleException(dto.getTitle());
        }
        Task task = Task.builder()
            .title(dto.getTitle())
            .category(dto.getCategory())
            .frequency(dto.getFrequency())
            .scheduledDate(dto.getScheduledDate())
            .build();
        Task saved = taskRepository.save(task);
        log.info("Created task: {}", saved.getId());
        return saved;
    }

    public Task updateTask(String id, TaskDTO dto) {
        Task existing = getTaskById(id);

        if (taskRepository.existsByTitleAndIdNot(dto.getTitle(), id)) {
            throw new DuplicateTitleException(dto.getTitle());
        }

        existing.setTitle(dto.getTitle());
        existing.setCategory(dto.getCategory());
        existing.setFrequency(dto.getFrequency());
        existing.setScheduledDate(dto.getScheduledDate());
        Task updated = taskRepository.save(existing);
        log.info("Updated task: {}", updated.getId());
        return updated;
    }

    public void deleteTask(String id) {
        getTaskById(id);
        taskStatusRepository.deleteByTaskId(id);
        taskRepository.deleteById(id);
        log.info("Deleted task {} and its status history.", id);
    }
}
