package com.growthtracker.service;

import com.growthtracker.exception.ResourceNotFoundException;
import com.growthtracker.model.Quote;
import com.growthtracker.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Returns a random motivational quote from the database.
 */
@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final Random random = new Random();

    public Quote getRandomQuote() {
        List<Quote> quotes = quoteRepository.findAll();
        if (quotes.isEmpty()) {
            throw new ResourceNotFoundException("No quotes found in the database.");
        }
        return quotes.get(random.nextInt(quotes.size()));
    }
}
