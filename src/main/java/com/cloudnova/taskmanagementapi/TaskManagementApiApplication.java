package com.cloudnova.taskmanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class for Task Management API
 *
 * This class serves as the entry point for the Spring Boot application.
 * The @SpringBootApplication annotation combines:
 * - @Configuration: Allows the class to be a source of bean definitions
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration mechanism
 * - @ComponentScan: Enables component scanning for the current package and sub-packages
 */
@SpringBootApplication
public class TaskManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApiApplication.class, args);
    }
}