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

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByTitleContainingIgnoreCase(String keyword);

    List<Task> findByCreatedAtAfter(LocalDateTime date);

    List<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status);

    boolean existsByTitle(String title);

    Optional<Task> findTopByOrderByCreatedAtDesc();

    @Query("SELECT t FROM Task t WHERE t.status IN :statuses ORDER BY t.updatedAt DESC")
    List<Task> findByStatusIn(@Param("statuses") List<TaskStatus> statuses);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Task> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT t FROM Task t WHERE " +
            "t.createdAt < :cutoffDate AND " +
            "t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    List<Task> findOverdueTasks(@Param("cutoffDate") LocalDateTime cutoffDate);
}