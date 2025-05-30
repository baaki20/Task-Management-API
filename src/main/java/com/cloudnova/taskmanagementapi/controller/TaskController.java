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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        logger.info("TaskController initialized with TaskService dependency");
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieve all tasks with optional filtering by status and search capabilities"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Tasks retrieved successfully",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(
            @Parameter(description = "Filter tasks by status (TODO, IN_PROGRESS, COMPLETED, CANCELLED)")
            @RequestParam(required = false) String status,
            @Parameter(description = "Search tasks by title or description")
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

    @Operation(
            summary = "Get task by ID",
            description = "Retrieve a specific task by its unique identifier"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Task found",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id) {

        logger.debug("GET /tasks/{}", id);

        Task task = taskService.getTaskById(id);
        TaskResponse taskResponse = TaskResponse.fromTask(task);

        ApiResponse<TaskResponse> response = ApiResponse.success(
                taskResponse,
                "Task retrieved successfully"
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create new task",
            description = "Create a new task with title, description, and optional status"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Task created successfully",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Task with title already exists",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Parameter(description = "Task creation request", required = true)
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

    @Operation(
            summary = "Update task",
            description = "Update an existing task by ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Task updated successfully",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Task with title already exists",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Task update request", required = true)
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

    @Operation(
            summary = "Delete task",
            description = "Delete a task by ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Task deleted successfully",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id) {

        logger.debug("DELETE /tasks/{}", id);

        taskService.deleteTask(id);

        ApiResponse<Void> response = ApiResponse.success(
                null,
                "Task deleted successfully"
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Complete task",
            description = "Mark a task as completed"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Task marked as completed",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            )
    })
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TaskResponse>> completeTask(
            @Parameter(description = "Task ID", required = true, example = "1")
            @PathVariable Long id) {

        logger.debug("PATCH /tasks/{}/complete", id);

        Task completedTask = taskService.completeTask(id);
        TaskResponse taskResponse = TaskResponse.fromTask(completedTask);

        ApiResponse<TaskResponse> response = ApiResponse.success(
                taskResponse,
                "Task marked as completed"
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get task statistics",
            description = "Retrieve statistics about task counts by status"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Statistics retrieved successfully",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class)
                    )}
            )
    })
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