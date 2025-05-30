package com.cloudnova.taskmanagementapi.dto;

import com.cloudnova.taskmanagementapi.model.Task;
import com.cloudnova.taskmanagementapi.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * TaskCreateRequest - DTO for creating new tasks
 */
public class TaskCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String status;

    public TaskCreateRequest() {}

    public TaskCreateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public TaskCreateRequest(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task toTask() {
        Task task = new Task();
        task.setTitle(this.title);
        task.setDescription(this.description);

        if (this.status != null && !this.status.trim().isEmpty()) {
            task.setStatus(TaskStatus.fromString(this.status));
        } else {
            task.setStatus(TaskStatus.TODO);
        }

        return task;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}