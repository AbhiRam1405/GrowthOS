package com.growthtracker.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a user-defined growth task (e.g., "Morning Run", "Read Books").
 * Title is unique at DB level via @Indexed(unique = true).
 * Auditing fields are auto-populated by @EnableMongoAuditing.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    private String category;

    /** "Daily" or "Weekly" — affects streak calculation */
    private String frequency;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
