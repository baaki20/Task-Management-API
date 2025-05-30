package com.cloudnova.taskmanagementapi.service;

import com.cloudnova.taskmanagementapi.model.Task;
import com.cloudnova.taskmanagementapi.model.TaskStatus;
import com.cloudnova.taskmanagementapi.repository.TaskRepository;
import com.cloudnova.taskmanagementapi.exception.TaskNotFoundException;
import com.cloudnova.taskmanagementapi.exception.DuplicateTaskException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TaskService - Business Logic Layer for Task operations
 *
 * This service demonstrates:
 * - Spring's Dependency Injection using @Autowired
 * - Service layer pattern for business logic encapsulation
 * - Transaction management with @Transactional
 * - Exception handling for business rules
 * - Logging integration
 *
 * The @Service annotation marks this as a service component
 * Spring's IoC container will manage this bean and inject dependencies
 */
@Service
@Transactional
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    // Dependency injection of TaskRepository
    // Spring IoC container will automatically inject the repository implementation
    private final TaskRepository taskRepository;

    /**
     * Constructor-based dependency injection (preferred approach)
     * @param taskRepository the task repository to inject
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        logger.info("TaskService initialized with TaskRepository dependency");
    }

    /**
     * Create a new task
     * @param task the task to create
     * @return the created task with generated ID
     * @throws DuplicateTaskException if task with same title already exists
     */
    public Task createTask(Task task) {
        logger.debug("Creating new task: {}", task.getTitle());

        // Business rule: Check for duplicate titles
        if (taskRepository.existsByTitle(task.getTitle())) {
            throw new DuplicateTaskException("Task with title '" + task.getTitle() + "' already exists");
        }

        // Set default values
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }

        Task savedTask = taskRepository.save(task);
        logger.info("Created task with ID: {}", savedTask.getId());

        return savedTask;
    }

    /**
     * Get all tasks
     * @return list of all tasks
     */
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        logger.debug("Retrieving all tasks");
        List<Task> tasks = taskRepository.findAll();
        logger.debug("Found {} tasks", tasks.size());
        return tasks;
    }

    /**
     * Get task by ID
     * @param id the task ID
     * @return the task
     * @throws TaskNotFoundException if task not found
     */
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        logger.debug("Retrieving task with ID: {}", id);

        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    /**
     * Update an existing task
     * @param id the task ID to update
     * @param updatedTask the updated task data
     * @return the updated task
     * @throws TaskNotFoundException if task not found
     */
    public Task updateTask(Long id, Task updatedTask) {
        logger.debug("Updating task with ID: {}", id);

        Task existingTask = getTaskById(id);

        // Update fields
        if (updatedTask.getTitle() != null && !updatedTask.getTitle().trim().isEmpty()) {
            // Check for duplicate title (excluding current task)
            if (!existingTask.getTitle().equals(updatedTask.getTitle()) &&
                    taskRepository.existsByTitle(updatedTask.getTitle())) {
                throw new DuplicateTaskException("Task with title '" + updatedTask.getTitle() + "' already exists");
            }
            existingTask.setTitle(updatedTask.getTitle());
        }

        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }

        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }

        Task savedTask = taskRepository.save(existingTask);
        logger.info("Updated task with ID: {}", savedTask.getId());

        return savedTask;
    }

    /**
     * Delete a task
     * @param id the task ID to delete
     * @throws TaskNotFoundException if task not found
     */
    public void deleteTask(Long id) {
        logger.debug("Deleting task with ID: {}", id);

        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }

        taskRepository.deleteById(id);
        logger.info("Deleted task with ID: {}", id);
    }

    /**
     * Get tasks by status
     * @param status the task status
     * @return list of tasks with the specified status
     */
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(TaskStatus status) {
        logger.debug("Retrieving tasks with status: {}", status);
        return taskRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    /**
     * Search tasks by keyword
     * @param keyword the search keyword
     * @return list of matching tasks
     */
    @Transactional(readOnly = true)
    public List<Task> searchTasks(String keyword) {
        logger.debug("Searching tasks with keyword: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTasks();
        }

        return taskRepository.searchByKeyword(keyword.trim());
    }

    /**
     * Get task statistics
     * @return task count by status
     */
    @Transactional(readOnly = true)
    public TaskStatistics getTaskStatistics() {
        logger.debug("Calculating task statistics");

        long todoCount = taskRepository.countByStatus(TaskStatus.TODO);
        long inProgressCount = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long completedCount = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long cancelledCount = taskRepository.countByStatus(TaskStatus.CANCELLED);
        long totalCount = taskRepository.count();

        return new TaskStatistics(todoCount, inProgressCount, completedCount, cancelledCount, totalCount);
    }

    /**
     * Mark task as completed
     * @param id the task ID
     * @return the updated task
     * @throws TaskNotFoundException if task not found
     */
    public Task completeTask(Long id) {
        logger.debug("Marking task as completed: {}", id);

        Task task = getTaskById(id);
        task.setStatus(TaskStatus.COMPLETED);

        Task savedTask = taskRepository.save(task);
        logger.info("Marked task {} as completed", id);

        return savedTask;
    }

    /**
     * Inner class for task statistics
     */
    public static class TaskStatistics {
        private final long todoCount;
        private final long inProgressCount;
        private final long completedCount;
        private final long cancelledCount;
        private final long totalCount;

        public TaskStatistics(long todoCount, long inProgressCount, long completedCount,
                              long cancelledCount, long totalCount) {
            this.todoCount = todoCount;
            this.inProgressCount = inProgressCount;
            this.completedCount = completedCount;
            this.cancelledCount = cancelledCount;
            this.totalCount = totalCount;
        }

        public long getTodoCount() { return todoCount; }
        public long getInProgressCount() { return inProgressCount; }
        public long getCompletedCount() { return completedCount; }
        public long getCancelledCount() { return cancelledCount; }
        public long getTotalCount() { return totalCount; }
    }
}