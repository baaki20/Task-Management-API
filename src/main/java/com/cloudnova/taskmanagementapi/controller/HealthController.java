package com.cloudnova.taskmanagementapi.controller;

import com.cloudnova.taskmanagementapi.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * HealthController - Basic health check endpoints
 *
 * This controller provides simple endpoints to verify the application is running
 * Useful for load balancers, monitoring systems, and Docker health checks
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    /**
     * GET /api/v1/health
     * Basic health check endpoint
     *
     * @return health status
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {

        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("application", "Task Management API");
        healthData.put("version", "1.0.0");

        ApiResponse<Map<String, Object>> response = ApiResponse.success(
                healthData,
                "Application is healthy"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/health/ping
     * Simple ping endpoint
     *
     * @return pong response
     */
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {

        ApiResponse<String> response = ApiResponse.success(
                "pong",
                "Service is responding"
        );

        return ResponseEntity.ok(response);
    }
}