package com.growthtracker.controller;

import com.growthtracker.model.DailySummary;
import com.growthtracker.service.DailySummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final DailySummaryService dailySummaryService;

    /**
     * GET /api/summary?date=YYYY-MM-DD
     * Returns the daily summary. If no record exists returns a blank summary.
     */
    @GetMapping
    public ResponseEntity<DailySummary> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(dailySummaryService.getSummary(date));
    }
}
