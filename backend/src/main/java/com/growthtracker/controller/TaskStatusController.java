package com.growthtracker.controller;

import com.growthtracker.dto.TaskWithStatusDTO;
import com.growthtracker.model.TaskStatus;
import com.growthtracker.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/status")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    /**
     * Mark a task as completed or not completed for a given date.
     * POST /api/status/{taskId}?date=YYYY-MM-DD&completed=true
     */
    @PostMapping("/{taskId}")
    public ResponseEntity<TaskStatus> markStatus(
            @PathVariable String taskId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "true") boolean completed) {
        return ResponseEntity.ok(taskStatusService.markStatus(taskId, date, completed));
    }

    /**
     * Get all tasks with their completion status for a given date.
     * GET /api/status?date=YYYY-MM-DD
     */
    @GetMapping
    public ResponseEntity<List<TaskWithStatusDTO>> getTasksWithStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(taskStatusService.getTasksWithStatus(date));
    }
}
