package com.cloudnova.taskmanagementapi.repository;

import com.cloudnova.taskmanagementapi.model.Task;
import com.cloudnova.taskmanagementapi.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TaskRepository - Data Access Layer for Task entities
 *
 * This repository demonstrates:
 * - Spring Data JPA repository pattern
 * - Automatic implementation by Spring IoC container
 * - Custom query methods using method naming conventions
 * - Custom JPQL queries for complex operations
 *
 * The @Repository annotation marks this as a Data Access Object (DAO)
 * Spring's IoC container will automatically create an implementation
 * and make it available for dependency injection.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Query methods using Spring Data JPA naming conventions
    // These methods are automatically implemented by Spring

    /**
     * Find tasks by status
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find tasks by title containing a keyword (case-insensitive)
     */
    List<Task> findByTitleContainingIgnoreCase(String keyword);

    /**
     * Find tasks created after a specific date
     */
    List<Task> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find tasks by status ordered by creation date descending
     */
    List<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status);

    /**
     * Check if a task exists with the given title
     */
    boolean existsByTitle(String title);

    /**
     * Find the most recently created task
     */
    Optional<Task> findTopByOrderByCreatedAtDesc();

    // Custom JPQL queries for more complex operations

    /**
     * Find tasks by multiple statuses using custom JPQL query
     */
    @Query("SELECT t FROM Task t WHERE t.status IN :statuses ORDER BY t.updatedAt DESC")
    List<Task> findByStatusIn(@Param("statuses") List<TaskStatus> statuses);

    /**
     * Count tasks by status
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);

    /**
     * Find tasks with title or description containing keyword
     */
    @Query("SELECT t FROM Task t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Find overdue tasks (created more than specified days ago and not completed)
     */
    @Query("SELECT t FROM Task t WHERE " +
            "t.createdAt < :cutoffDate AND " +
            "t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    List<Task> findOverdueTasks(@Param("cutoffDate") LocalDateTime cutoffDate);
}