package com.growthtracker.repository;

import com.growthtracker.model.DailySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailySummaryRepository extends MongoRepository<DailySummary, String> {

    Optional<DailySummary> findByDate(String date);

    List<DailySummary> findByDateBetweenOrderByDateAsc(String from, String to);

    List<DailySummary> findAllByOrderByDateDesc();
}
