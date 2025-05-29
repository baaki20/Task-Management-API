# Use official OpenJDK 21 runtime as base image
FROM openjdk:21-jdk-slim

# Install required packages
RUN apt-get update && \
    apt-get install -y curl dos2unix && \
    rm -rf /var/lib/apt/lists/*

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and pom.xml for dependency caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN sed -i 's/\r$//' mvnw
RUN chmod +x ./mvnw

# Download dependencies with retry (handles transient network issues)
RUN until ./mvnw dependency:go-offline -B; do echo "Retrying dependency download..."; sleep 5; done

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown -R appuser:appuser /app
USER appuser

# Run the application
CMD ["java", "-jar", "target/task-management-api-1.0.0.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/health/ping || exit 1