package com.growthtracker.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Tracks whether a specific task was completed on a specific date.
 * Compound index on (taskId, date) ensures one record per task per day
 * and enables fast lookups for daily status queries.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "taskStatus")
@CompoundIndexes({
    @CompoundIndex(name = "taskId_date_idx", def = "{'taskId': 1, 'date': 1}", unique = true)
})
public class TaskStatus {

    @Id
    private String id;

    private String taskId;

    private LocalDate date;

    private boolean completed;
}
