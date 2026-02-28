package com.growthtracker.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Motivational quote document.
 * Seeds are inserted by DataInitializer on startup if the collection is empty.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "quotes")
public class Quote {

    @Id
    private String id;

    private String quoteText;
}
