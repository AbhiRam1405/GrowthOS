package com.growthtracker.repository;

import com.growthtracker.model.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskStatusRepository extends MongoRepository<TaskStatus, String> {

    Optional<TaskStatus> findByTaskIdAndDate(String taskId, LocalDate date);

    List<TaskStatus> findByDate(LocalDate date);

    List<TaskStatus> findByTaskId(String taskId);

    List<TaskStatus> findByTaskIdAndDateBetween(String taskId, LocalDate from, LocalDate to);

    List<TaskStatus> findByDateBetween(LocalDate from, LocalDate to);

    void deleteByTaskId(String taskId);
}
