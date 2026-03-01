package com.growthtracker.service;

import com.growthtracker.dto.CompleteTaskRequest;
import com.growthtracker.dto.TaskDTO;
import com.growthtracker.dto.TaskHistoryFilterRequest;
import com.growthtracker.exception.DuplicateTitleException;
import com.growthtracker.exception.ResourceNotFoundException;
import com.growthtracker.model.Task;
import com.growthtracker.repository.TaskRepository;
import com.growthtracker.repository.TaskStatusRepository;
import com.growthtracker.model.Priority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Task CRUD operations.
 * Enforces unique title constraint at the service layer (complementing the DB index).
 * Cascades deletes to TaskStatus records to avoid orphaned data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final MongoTemplate mongoTemplate;

    public List<Task> getAllTasks() {
        return taskRepository.findAll().stream()
            .sorted((t1, t2) -> {
                // Primary: Status (PENDING before COMPLETED)
                int statusCompare = t1.getStatus().compareTo(t2.getStatus()); // "COMPLETED" > "PENDING" ? No, C < P. 
                // Wait, "COMPLETED" starts with C, "PENDING" starts with P. 
                // C comes before P. So compareTo will return < 0 if t1 is COMPLETED.
                // We want PENDING first. So we should reverse it or use custom logic.
                
                boolean t1Pending = !"COMPLETED".equals(t1.getStatus());
                boolean t2Pending = !"COMPLETED".equals(t2.getStatus());
                
                if (t1Pending && !t2Pending) return -1;
                if (!t1Pending && t2Pending) return 1;
                
                // Secondary: Priority (Weight DESC)
                return Integer.compare(t2.getPriority().getWeight(), t1.getPriority().getWeight());
            })
            .toList();
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public Task createTask(TaskDTO dto) {
        if (taskRepository.existsByTitle(dto.getTitle())) {
            throw new DuplicateTitleException(dto.getTitle());
        }
        Task task = Task.builder()
            .title(dto.getTitle())
            .category(dto.getCategory())
            .frequency(dto.getFrequency())
            .scheduledDate(dto.getScheduledDate())
            .priority(dto.getPriority() != null ? dto.getPriority() : Priority.MEDIUM)
            .build();
        Task saved = taskRepository.save(task);
        log.info("Created task: {}", saved.getId());
        return saved;
    }

    public Task updateTask(String id, TaskDTO dto) {
        Task existing = getTaskById(id);

        if (taskRepository.existsByTitleAndIdNot(dto.getTitle(), id)) {
            throw new DuplicateTitleException(dto.getTitle());
        }

        existing.setTitle(dto.getTitle());
        existing.setCategory(dto.getCategory());
        existing.setFrequency(dto.getFrequency());
        existing.setScheduledDate(dto.getScheduledDate());
        existing.setPriority(dto.getPriority() != null ? dto.getPriority() : Priority.MEDIUM);
        Task updated = taskRepository.save(existing);
        log.info("Updated task: {}", updated.getId());
        return updated;
    }

    public Task completeTask(String id, CompleteTaskRequest request) {
        Task task = getTaskById(id);

        if ("COMPLETED".equals(task.getStatus())) {
            throw new IllegalStateException("Task already completed");
        }

        task.setStatus("COMPLETED");
        task.setCompletionNote(request.getNote());
        task.setTimeSpent(request.getTimeSpent());
        task.setCompletedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);
        log.info("Task {} marked as COMPLETED", id);
        return saved;
    }

    public void deleteTask(String id) {
        getTaskById(id);
        taskStatusRepository.deleteByTaskId(id);
        taskRepository.deleteById(id);
        log.info("Deleted task {} and its status history.", id);
    }

    public List<Task> getTaskHistory(TaskHistoryFilterRequest filters) {
        // 1. Date Validation
        if (filters.getStartDate() != null && filters.getEndDate() != null 
                && filters.getStartDate().isAfter(filters.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Query query = new Query();
        
        // 2. Always filter status = "COMPLETED"
        Criteria criteria = Criteria.where("status").is("COMPLETED");

        // 3. Optional Filters
        if (StringUtils.hasText(filters.getCategory())) {
            criteria.and("category").is(filters.getCategory());
        }

        if (filters.getStartDate() != null && filters.getEndDate() != null) {
            criteria.and("completedAt").gte(filters.getStartDate().atStartOfDay())
                                       .lte(filters.getEndDate().atTime(LocalTime.MAX));
        } else if (filters.getStartDate() != null) {
            criteria.and("completedAt").gte(filters.getStartDate().atStartOfDay());
        } else if (filters.getEndDate() != null) {
            criteria.and("completedAt").lte(filters.getEndDate().atTime(LocalTime.MAX));
        }

        if (filters.getMinTimeSpent() != null && filters.getMaxTimeSpent() != null) {
            criteria.and("timeSpent").gte(filters.getMinTimeSpent()).lte(filters.getMaxTimeSpent());
        } else if (filters.getMinTimeSpent() != null) {
            criteria.and("timeSpent").gte(filters.getMinTimeSpent());
        } else if (filters.getMaxTimeSpent() != null) {
            criteria.and("timeSpent").lte(filters.getMaxTimeSpent());
        }

        if (StringUtils.hasText(filters.getSearchKeyword())) {
            criteria.and("title").regex(filters.getSearchKeyword(), "i");
        }

        if (filters.getPriority() != null) {
            criteria.and("priority").is(filters.getPriority());
        }

        query.addCriteria(criteria);

        // 4. Sorting
        String sortBy = filters.getSortBy();
        if ("oldest".equalsIgnoreCase(sortBy)) {
            query.with(Sort.by(Sort.Direction.ASC, "completedAt"));
        } else if ("time_desc".equalsIgnoreCase(sortBy)) {
            query.with(Sort.by(Sort.Direction.DESC, "timeSpent"));
        } else if ("priority".equalsIgnoreCase(sortBy)) {
            // High to Low: URGENT > HIGH > MEDIUM > LOW
            // Note: MongoDB enum sorting is usually alphabetical by default unless custom logic is applied.
            // For true priority sorting, we might need a custom weight or use a switch/case in aggregation.
            // However, common practice is to use Sort.by(Sort.Direction.DESC, "priority") if the enum order matches.
            // Let's use string-based or we'll have to manually handle it if needed.
            // The user asked for URGENT > HIGH > MEDIUM > LOW. Alphabetically: U, H, M, L.
            // That's U(RGENT), M(EDIUM), L(OW), H(IGH) - doesn't work.
            // I will use a simple switch/case or just sort by the field and warn user if enum order is not alphabetical.
            // Alternatively, I'll use a manual Sort if possible or just DESC on priority.
            query.with(Sort.by(Sort.Direction.DESC, "priority")); 
        } else {
            // Default: latest completed first
            query.with(Sort.by(Sort.Direction.DESC, "completedAt"));
        }

        // 5. Pagination
        int page = (filters.getPage() != null && filters.getPage() >= 0) ? filters.getPage() : 0;
        int size = (filters.getSize() != null && filters.getSize() > 0) ? filters.getSize() : 10;
        
        // If sorting by priority, we need custom weights
        if ("priority".equalsIgnoreCase(sortBy)) {
            // Using Aggregation for custom sort weights
            AggregationOperation match = Aggregation.match(criteria);
            
            // Add weight field: URGENT(3), HIGH(2), MEDIUM(1), LOW(0)
            AggregationOperation addWeight = Aggregation.addFields()
                .addField("prioWeight")
                .withValue(ConditionalOperators.when(Criteria.where("priority").is("URGENT")).then(Priority.URGENT.getWeight())
                    .otherwise(ConditionalOperators.when(Criteria.where("priority").is("HIGH")).then(Priority.HIGH.getWeight())
                        .otherwise(ConditionalOperators.when(Criteria.where("priority").is("MEDIUM")).then(Priority.MEDIUM.getWeight())
                            .otherwise(Priority.LOW.getWeight()))))
                .build();
            
            AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, "prioWeight");
            AggregationOperation skip = Aggregation.skip((long) page * size);
            AggregationOperation limit = Aggregation.limit(size);
            
            TypedAggregation<Task> aggregation = Aggregation.newAggregation(Task.class, match, addWeight, sort, skip, limit);
            return mongoTemplate.aggregate(aggregation, Task.class).getMappedResults();
        }

        // Standard Query for other cases
        query.with(PageRequest.of(page, size));
        return mongoTemplate.find(query, Task.class);
    }
}
