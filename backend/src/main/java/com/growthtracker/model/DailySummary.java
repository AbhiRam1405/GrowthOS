package com.growthtracker.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Daily summary document — one per day.
 * Uses YYYY-MM-DD date string as the document _id for O(1) lookup.
 *
 * Streak logic:
 *   - No Daily tasks exist → streak = 0
 *   - No yesterday summary → streak resets to 0 (then +1 if today ≥ 70%)
 *   - completionPercentage >= 70 → streak = yesterdayStreak + 1
 *   - Otherwise → streak = 0
 *
 * longestStreak is persisted in DB (updated whenever streak increases).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "dailySummary")
public class DailySummary {

    /** The date string "YYYY-MM-DD" used as the document ID */
    @Id
    private String id;

    @Indexed
    private String date;

    private int totalTasks;

    private int completedTasks;

    private double completionPercentage;

    /** Current streak at this date */
    private int streak;

    /** Highest streak ever recorded — persisted in DB, not computed dynamically */
    private int longestStreak;
}
