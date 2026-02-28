package com.growthtracker.repository;

import com.growthtracker.model.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends MongoRepository<Quote, String> {
}
