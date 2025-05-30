package com.cloudnova.taskmanagementapi.dto;

import com.cloudnova.taskmanagementapi.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * TaskResponse - DTO for task responses
 */
@Schema(description = "Task response payload")
public class TaskResponse {

    @Schema(description = "Task unique identifier", example = "1")
    private Long id;

    @Schema(description = "Task title", example = "Complete project documentation")
    private String title;

    @Schema(description = "Task description", example = "Write comprehensive API documentation")
    private String description;

    @Schema(description = "Task status code", example = "TODO")
    private String status;

    @Schema(description = "Task status display name", example = "To Do")
    private String statusDisplay;

    @Schema(description = "Task creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Task last update timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;

    // Constructors
    public TaskResponse() {}

    public TaskResponse(Long id, String title, String description, String status,
                        String statusDisplay, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.statusDisplay = statusDisplay;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory method to create TaskResponse from Task entity
    public static TaskResponse fromTask(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getStatus().getDisplayName(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusDisplay() { return statusDisplay; }
    public void setStatusDisplay(String statusDisplay) { this.statusDisplay = statusDisplay; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}