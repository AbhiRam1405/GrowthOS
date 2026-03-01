package com.growthtracker.dto;

import com.growthtracker.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistoryFilterRequest {
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer minTimeSpent;
    private Integer maxTimeSpent;
    private String searchKeyword;
    private String sortBy;
    private Priority priority;
    
    @Builder.Default
    private Integer page = 0;
    
    @Builder.Default
    private Integer size = 10;
}
