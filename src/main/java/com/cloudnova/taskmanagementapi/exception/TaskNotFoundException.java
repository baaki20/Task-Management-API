package com.cloudnova.taskmanagementapi.exception;

/**
 * TaskNotFoundException - Custom exception for when a task is not found
 *
 * This exception is thrown when attempting to access a task that doesn't exist
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

