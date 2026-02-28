package com.growthtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO for creating or updating a task.
 */
@Data
public class TaskDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Frequency is required")
    @Pattern(regexp = "Daily|Weekly", message = "Frequency must be 'Daily' or 'Weekly'")
    private String frequency;
}
