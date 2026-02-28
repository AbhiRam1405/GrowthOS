package com.growthtracker.config;

import com.growthtracker.model.Quote;
import com.growthtracker.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the quotes collection with motivational quotes on application startup
 * if the collection is empty.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final QuoteRepository quoteRepository;

    @Override
    public void run(String... args) {
        if (quoteRepository.count() == 0) {
            log.info("Seeding motivational quotes...");
            List<Quote> quotes = List.of(
                Quote.builder().quoteText("The secret of getting ahead is getting started. – Mark Twain").build(),
                Quote.builder().quoteText("It always seems impossible until it's done. – Nelson Mandela").build(),
                Quote.builder().quoteText("Don't watch the clock; do what it does. Keep going. – Sam Levenson").build(),
                Quote.builder().quoteText("Success is not the key to happiness. Happiness is the key to success. – Albert Schweitzer").build(),
                Quote.builder().quoteText("Believe you can and you're halfway there. – Theodore Roosevelt").build(),
                Quote.builder().quoteText("The only way to do great work is to love what you do. – Steve Jobs").build(),
                Quote.builder().quoteText("In the middle of every difficulty lies opportunity. – Albert Einstein").build(),
                Quote.builder().quoteText("You miss 100% of the shots you don't take. – Wayne Gretzky").build(),
                Quote.builder().quoteText("Success is walking from failure to failure with no loss of enthusiasm. – Winston Churchill").build(),
                Quote.builder().quoteText("Hardships often prepare ordinary people for an extraordinary destiny. – C.S. Lewis").build(),
                Quote.builder().quoteText("Don't let yesterday take up too much of today. – Will Rogers").build(),
                Quote.builder().quoteText("You don't have to be great to start, but you have to start to be great. – Zig Ziglar").build(),
                Quote.builder().quoteText("The future depends on what you do today. – Mahatma Gandhi").build(),
                Quote.builder().quoteText("Push yourself, because no one else is going to do it for you.").build(),
                Quote.builder().quoteText("Small steps every day lead to massive results over time.").build()
            );
            quoteRepository.saveAll(quotes);
            log.info("Seeded {} motivational quotes.", quotes.size());
        }
    }
}
