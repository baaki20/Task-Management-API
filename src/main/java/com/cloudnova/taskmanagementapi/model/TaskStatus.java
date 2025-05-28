package com.cloudnova.taskmanagementapi.model;

/**
 * TaskStatus Enum - Represents the possible states of a task
 *
 * This enum defines the lifecycle states of a task:
 * - TODO: Task is created but not started
 * - IN_PROGRESS: Task is currently being worked on
 * - COMPLETED: Task has been finished
 * - CANCELLED: Task has been cancelled
 */
public enum TaskStatus {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get TaskStatus from string value (case-insensitive)
     * @param value the string value to convert
     * @return TaskStatus enum value
     * @throws IllegalArgumentException if value doesn't match any status
     */
    public static TaskStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return TODO; // Default status
        }

        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equalsIgnoreCase(value.trim()) ||
                    status.displayName.equalsIgnoreCase(value.trim())) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid task status: " + value);
    }

    @Override
    public String toString() {
        return displayName;
    }
}