package com.cloudnova.taskmanagementapi.config;

import com.cloudnova.taskmanagementapi.model.Task;
import com.cloudnova.taskmanagementapi.model.TaskStatus;
import com.cloudnova.taskmanagementapi.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - Initialize sample data for development and testing
 *
 * This component demonstrates:
 * - Spring Boot's CommandLineRunner interface
 * - Data initialization at application startup
 * - Sample data creation for testing purposes
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final TaskRepository taskRepository;

    @Autowired
    public DataInitializer(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initializing sample data...");

        // Check if data already exists
        if (taskRepository.count() > 0) {
            logger.info("Data already exists, skipping initialization");
            return;
        }

        // Create sample tasks
        createSampleTasks();

        logger.info("Sample data initialization completed. Total tasks: {}", taskRepository.count());
    }

    private void createSampleTasks() {
        // Sample tasks with different statuses
        Task[] sampleTasks = {
                new Task("Complete project documentation",
                        "Write comprehensive API documentation with examples and usage guides",
                        TaskStatus.TODO),

                new Task("Implement user authentication",
                        "Add JWT-based authentication system for secure API access",
                        TaskStatus.IN_PROGRESS),

                new Task("Set up CI/CD pipeline",
                        "Configure GitHub Actions for automated testing and deployment",
                        TaskStatus.TODO),

                new Task("Database optimization",
                        "Optimize database queries and add proper indexing",
                        TaskStatus.IN_PROGRESS),

                new Task("Write unit tests",
                        "Implement comprehensive unit tests for all service methods",
                        TaskStatus.COMPLETED),

                new Task("Deploy to production",
                        "Deploy the application to AWS ECS with proper monitoring",
                        TaskStatus.TODO),

                new Task("Security audit",
                        "Conduct thorough security audit and fix vulnerabilities",
                        TaskStatus.CANCELLED),

                new Task("Performance testing",
                        "Run load tests and optimize application performance",
                        TaskStatus.TODO),

                new Task("Code review process",
                        "Establish code review guidelines and implement peer review process",
                        TaskStatus.COMPLETED),

                new Task("API versioning strategy",
                        "Define and implement API versioning strategy for backward compatibility",
                        TaskStatus.IN_PROGRESS)
        };

        // Save all sample tasks
        for (Task task : sampleTasks) {
            taskRepository.save(task);
            logger.debug("Created sample task: {}", task.getTitle());
        }
    }
}