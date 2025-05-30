# Task Management API - Architecture Documentation

## Component Architecture Diagram

> **Note:** To view the diagram, copy the code below and paste it into a [Mermaid Live Editor](https://mermaid.live/) or use a Markdown viewer that supports Mermaid diagrams.

```
graph TB
    %% External Layer
    Client[Client Applications<br/>Web/Mobile/API Clients]
    
    %% Presentation Layer
    subgraph "Presentation Layer"
        TC[TaskController<br/>REST Endpoints]
        HC[HealthController<br/>Health Checks]
        GEH[GlobalExceptionHandler<br/>Error Handling]
    end
    
    %% Service Layer
    subgraph "Business Logic Layer"
        TS[TaskService<br/>Business Logic]
    end
    
    %% Data Transfer Objects
    subgraph "Data Transfer Objects"
        TCR[TaskCreateRequest]
        TUR[TaskUpdateRequest]
        TR[TaskResponse]
        AR[ApiResponse<T>]
    end
    
    %% Repository Layer
    subgraph "Data Access Layer"
        TaskRepo[TaskRepository<br/>JPA Repository]
    end
    
    %% Domain Models
    subgraph "Domain Model"
        Task[Task Entity<br/>@Entity]
        TaskStatus[TaskStatus Enum<br/>Status Values]
    end
    
    %% Exception Handling
    subgraph "Exception Handling"
        TNF[TaskNotFoundException]
        DTE[DuplicateTaskException]
    end
    
    %% Configuration & Infrastructure
    subgraph "Configuration & Infrastructure"
        App[TaskManagementApiApplication<br/>@SpringBootApplication]
        DI[DataInitializer<br/>Sample Data]
        Config[application.yml<br/>Configuration]
    end
    
    %% Database
    DB[(H2 Database<br/>In-Memory)]
    
    %% External interactions
    Client --> TC
    Client --> HC
    
    %% Controller layer interactions
    TC --> TS
    TC --> TCR
    TC --> TUR
    TC --> TR
    TC --> AR
    HC --> AR
    
    %% Exception handling
    TC -.-> GEH
    TS -.-> GEH
    TS --> TNF
    TS --> DTE
    GEH --> AR
    
    %% Service layer interactions
    TS --> TaskRepo
    TS --> Task
    TS --> TaskStatus
    
    %% Repository interactions
    TaskRepo --> DB
    TaskRepo --> Task
    
    %% Domain model relationships
    Task --> TaskStatus
    
    %% DTO transformations
    TCR -.-> Task
    TUR -.-> Task
    TR -.-> Task
    
    %% Infrastructure
    App --> DI
    App --> Config
    DI --> TaskRepo
    DI --> Task
    DI --> TaskStatus
    
    %% Styling
    classDef controller fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef service fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef repository fill:#e8f5e8,stroke:#1b5e20,stroke-width:2px
    classDef model fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef dto fill:#fce4ec,stroke:#880e4f,stroke-width:2px
    classDef exception fill:#ffebee,stroke:#b71c1c,stroke-width:2px
    classDef config fill:#f1f8e9,stroke:#33691e,stroke-width:2px
    classDef database fill:#e0f2f1,stroke:#004d40,stroke-width:3px
    classDef external fill:#f5f5f5,stroke:#424242,stroke-width:2px
    
    class TC,HC,GEH controller
    class TS service
    class TaskRepo repository
    class Task,TaskStatus model
    class TCR,TUR,TR,AR dto
    class TNF,DTE exception
    class App,DI,Config config
    class DB database
    class Client external
```

## Architecture Overview

### Layers Description

#### **Presentation Layer**
- **TaskController**: Handles all REST API endpoints for task CRUD operations
- **HealthController**: Provides system health monitoring endpoints
- **GlobalExceptionHandler**: Centralized exception handling with consistent error responses

#### **Business Logic Layer**
- **TaskService**: Contains core business rules, validation logic, and orchestrates data operations

#### **Data Access Layer**
- **TaskRepository**: JPA repository interface with custom query methods for database operations

#### **Domain Model**
- **Task Entity**: Core business entity representing tasks with JPA annotations
- **TaskStatus Enum**: Enumeration defining task lifecycle states

#### **Data Transfer Objects**
- **Request DTOs**: Handle input validation and data binding
- **Response DTOs**: Provide consistent API response structure

### Design Patterns

1. **Layered Architecture**: Clear separation of concerns across presentation, business, and data layers
2. **Dependency Injection**: Spring IoC container manages component dependencies
3. **Repository Pattern**: Abstracts data access logic from business logic
4. **DTO Pattern**: Separates internal domain models from API contracts
5. **Exception Handling**: Centralized error management with custom exceptions

### Key Features

- RESTful API design with proper HTTP methods and status codes
- Comprehensive input validation using Bean Validation
- Custom exception handling with meaningful error messages
- JPA/Hibernate integration with H2 in-memory database
- Automatic API documentation capabilities
- Health check endpoints for monitoring
- Sample data initialization for development/testing
