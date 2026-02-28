package com.growthtracker.controller;

import com.growthtracker.model.Quote;
import com.growthtracker.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    /** GET /api/quotes/random */
    @GetMapping("/random")
    public ResponseEntity<Quote> getRandomQuote() {
        return ResponseEntity.ok(quoteService.getRandomQuote());
    }
}
