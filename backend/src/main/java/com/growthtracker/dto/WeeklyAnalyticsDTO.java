package com.growthtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for the weekly analytics endpoint.
 * Includes averages, streaks, task performance, and daily progress breakdown.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyAnalyticsDTO {

    private double weeklyAverage;
    private int currentStreak;
    private int longestStreak;
    private String weakestTask;
    private String strongestTask;

    /** Daily progress for the last 7 days — [{date, completionPercentage}] */
    private List<DailyProgress> dailyProgress;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyProgress {
        private String date;
        private double completionPercentage;
    }
}
