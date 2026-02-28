package com.growthtracker.controller;

import com.growthtracker.dto.WeeklyAnalyticsDTO;
import com.growthtracker.service.AnalyticsService;
import com.growthtracker.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final SuggestionService suggestionService;

    /** GET /api/analytics/weekly */
    @GetMapping("/weekly")
    public ResponseEntity<WeeklyAnalyticsDTO> getWeeklyAnalytics() {
        return ResponseEntity.ok(analyticsService.getWeeklyAnalytics());
    }

    /** GET /api/analytics/category — returns {"Health": 5, "Coding": 3, ...} */
    @GetMapping("/category")
    public ResponseEntity<Map<String, Long>> getCategoryAnalytics() {
        return ResponseEntity.ok(analyticsService.getCategoryAnalytics());
    }

    /** GET /api/suggestions */
    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, String>> getSuggestion() {
        return ResponseEntity.ok(Map.of("suggestion", suggestionService.getSuggestion()));
    }
}
