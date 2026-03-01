package com.growthtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for marking a task as complete.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskRequest {

    @NotBlank(message = "Completion note cannot be empty")
    @Size(max = 2000, message = "Note cannot exceed 2000 characters")
    private String note;

    private Integer timeSpent;
}
