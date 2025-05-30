package com.cloudnova.taskmanagementapi.controller;

import com.cloudnova.taskmanagementapi.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Health Check", description = "Health monitoring endpoints")
public class HealthController {

    @Operation(
            summary = "Health check",
            description = "Check application health status"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Application is healthy",
                    content = {@io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                    )}
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Application is unhealthy",
                    content = {@io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ApiResponse.class)
                    )}
            )
    })
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

    @Operation(
            summary = "Ping endpoint",
            description = "Simple ping-pong endpoint to verify service availability"
    )

    /**
     * GET /api/v1/health/ping
     * Simple ping endpoint
     *
     * @return pong response
     */
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping(){

        ApiResponse<String> response = ApiResponse.success(
                "pong",
                "Service is responding"
        );

        return ResponseEntity.ok(response);
    }
}