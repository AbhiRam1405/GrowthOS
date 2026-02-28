package com.growthtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO returned when querying task completion status for a given date.
 * Combines Task details with its completion status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskWithStatusDTO {

    private String taskId;
    private String title;
    private String category;
    private String frequency;
    private java.time.LocalDate scheduledDate;
    private boolean completed;
}
