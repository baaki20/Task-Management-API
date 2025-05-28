package com.cloudnova.taskmanagementapi.controller;

import com.cloudnova.taskmanagementapi.dto.TaskCreateRequest;
import com.cloudnova.taskmanagementapi.dto.TaskUpdateRequest;
import com.cloudnova.taskmanagementapi.dto.TaskResponse;
import com.cloudnova.taskmanagementapi.dto.ApiResponse;
import com.cloudnova.taskmanagementapi.model.Task;
import com.cloudnova.taskmanagementapi.model.TaskStatus;
import com.cloudnova.taskmanagementapi.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TaskController - REST API Controller for Task Management
 *
 * This controller demonstrates:
 * - Spring MVC REST controller pattern
 * - Dependency injection of service layer
 * - RESTful API design with proper HTTP methods and status codes
 * - Request/Response DTOs for API contracts
 * - Input validation using Bean Validation
 * - Exception handling and error responses
 *
 * Base URL: /api/v1/tasks
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*") // Enable CORS for frontend integration
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    // Dependency injection of TaskService
    // Spring IoC container automatically injects the service implementation
    private final TaskService taskService;

    /**
     * Constructor-based dependency injection
     * @param taskService the task service to inject
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        logger.info("TaskController initialized with TaskService dependency");
    }

    /**
     * GET /api/v1/tasks
     * Retrieve all tasks or filter by status
     *
     * @param status optional status filter
     * @param search optional search keyword
     * @return list of tasks
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {

        logger.debug("GET /tasks - status: {}, search: {}", status, search);

        List<Task> tasks;

        if (search != null && !search.trim().isEmpty()) {
            tasks = taskService.searchTasks(search);
        } else if (status != null && !status.trim().isEmpty()) {
            TaskStatus taskStatus = TaskStatus.fromString(status);
            tasks = taskService.getTasksByStatus(taskStatus);
        } else {
            tasks = taskService.getAllTasks();
        }

        List<TaskResponse> taskResponses = tasks.stream()
                .map(TaskResponse::fromTask)
                .collect(Collectors.toList());

        ApiResponse<List<TaskResponse>> response = ApiResponse.success(
                taskResponses,
                "Tasks retrieved successfully"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/tasks/{id}
     * Retrieve a specific task by ID
     *
     * @param id the task ID
     * @return task details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {

        logger.debug("GET /tasks/{}", id);

        Task task = taskService.getTaskById(id);
        TaskResponse taskResponse = TaskResponse.fromTask(task);

        ApiResponse<TaskResponse> response = ApiResponse.success(
                taskResponse,
                "Task retrieved successfully"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/tasks
     * Create a new task
     *
     * @param createRequest the task creation request
     * @return created task details
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskCreateRequest createRequest) {

        logger.debug("POST /tasks - Creating task: {}", createRequest.getTitle());

        Task task = createRequest.toTask();
        Task createdTask = taskService.createTask(task);
        TaskResponse taskResponse = TaskResponse.fromTask(createdTask);

        ApiResponse<TaskResponse> response = ApiResponse.success(
                taskResponse,
                "Task created successfully"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/v1/tasks/{id}
     * Update an existing task
     *
     * @param id the task ID to update
     * @param updateRequest the task update request
     * @return updated task details
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest updateRequest) {

        logger.debug("PUT /tasks/{} - Updating task", id);

        Task task = updateRequest.toTask();
        Task updatedTask = taskService.updateTask(id, task);
        TaskResponse taskResponse = TaskResponse.fromTask(updatedTask);

        ApiResponse<TaskResponse> response = ApiResponse.success(
                taskResponse,
                "Task updated successfully"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/tasks/{id}
     * Delete a task
     *
     * @param id the task ID to delete
     * @return success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {

        logger.debug("DELETE /tasks/{}", id);

        taskService.deleteTask(id);

        ApiResponse<Void> response = ApiResponse.success(
                null,
                "Task deleted successfully"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/v1/tasks/{id}/complete
     * Mark a task as completed
     *
     * @param id the task ID to complete
     * @return updated task details
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TaskResponse>> completeTask(@PathVariable Long id) {

        logger.debug("PATCH /tasks/{}/complete", id);

        Task completedTask = taskService.completeTask(id);
        TaskResponse taskResponse = TaskResponse.fromTask(completedTask);

        ApiResponse<TaskResponse> response = ApiResponse.success(
                taskResponse,
                "Task marked as completed"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/tasks/statistics
     * Get task statistics
     *
     * @return task statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<TaskService.TaskStatistics>> getTaskStatistics() {

        logger.debug("GET /tasks/statistics");

        TaskService.TaskStatistics statistics = taskService.getTaskStatistics();

        ApiResponse<TaskService.TaskStatistics> response = ApiResponse.success(
                statistics,
                "Task statistics retrieved successfully"
        );

        return ResponseEntity.ok(response);
    }
}