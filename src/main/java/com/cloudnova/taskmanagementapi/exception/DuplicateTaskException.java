package com.cloudnova.taskmanagementapi.exception;

/**
 * DuplicateTaskException - Custom exception for duplicate task scenarios
 *
 * This exception is thrown when attempting to create a task with a title that already exists
 */
public class DuplicateTaskException extends RuntimeException {

    public DuplicateTaskException(String message) {
        super(message);
    }

    public DuplicateTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}