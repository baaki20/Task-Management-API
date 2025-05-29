# Task Management API

A RESTful Task Management API built with Spring Boot, demonstrating modern Java web development practices with containerization support.

## 🚀 Features

- **Full CRUD Operations** for task management
- **RESTful API Design** following REST principles
- **Spring Boot Framework** with modern Java 21
- **Spring IoC Container** with dependency injection
- **In-memory H2 Database** for development
- **Input Validation** with Bean Validation
- **Global Exception Handling** with consistent error responses
- **API Documentation** with examples
- **Docker Containerization** ready
- **Health Check Endpoints** for monitoring

## 🏗️ Architecture

The application follows a layered architecture:

```
├── Controller Layer (REST endpoints)
├── Service Layer (Business logic)
├── Repository Layer (Data access)
├── Model Layer (Entities and DTOs)
└── Exception Handling (Global error management)
```

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Docker** (optional, for containerization)
- **Git**

## 🛠️ Installation & Setup

### Method 1: Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/baaki20/Task-Management-API.git

   cd "Task Management API"
   ```

2. **Build the application**
   ```bash
   ./mvnw clean package
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**
    - API Base URL: `http://localhost:8080/api/v1`
    - H2 Console: `http://localhost:8080/api/v1/h2-console`

### Method 2: Docker Containerization

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Or build and run manually**
   ```bash
   # Build the image
   docker build -t task-management-api .
   
   # Run the container
   docker run -p 8080:8080 task-management-api
   ```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Task Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/tasks` | Get all tasks (supports filtering) |
| GET | `/tasks/{id}` | Get task by ID |
| POST | `/tasks` | Create new task |
| PUT | `/tasks/{id}` | Update existing task |
| DELETE | `/tasks/{id}` | Delete task |
| PATCH | `/tasks/{id}/complete` | Mark task as completed |
| GET | `/tasks/statistics` | Get task statistics |

### Health Check Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Application health status |
| GET | `/health/ping` | Simple ping endpoint |

### Task Status Values

- `TODO` - Task is created but not started
- `IN_PROGRESS` - Task is currently being worked on
- `COMPLETED` - Task has been finished
- `CANCELLED` - Task has been cancelled

## 🧪 API Testing Examples

### Create a Task
```bash
curl -X POST "http://localhost:8080/api/v1/tasks" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive API documentation",
    "status": "TODO"
  }'
```

### Get All Tasks
```bash
curl -X GET "http://localhost:8080/api/v1/tasks"
```

### Filter Tasks by Status
```bash
curl -X GET "http://localhost:8080/api/v1/tasks?status=TODO"
```

### Search Tasks
```bash
curl -X GET "http://localhost:8080/api/v1/tasks?search=documentation"
```

### Update a Task
```bash
curl -X PUT "http://localhost:8080/api/v1/tasks/1" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated task title",
    "description": "Updated description",
    "status": "IN_PROGRESS"
  }'
```

### Get Task Statistics
```bash
curl -X GET "http://localhost:8080/api/v1/tasks/statistics"
```

## 🗄️ Database Access

### H2 Console (Development)
- URL: `http://localhost:8080/api/v1/h2-console`
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: `password`

## 🐳 Docker Commands

### Build Image
```bash
docker build -t task-management-api .
```

### Run Container
```bash
docker run -p 8080:8080 task-management-api
```

### Using Docker Compose
```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f task-api

# Stop services
docker-compose down
```

## 🧪 Testing

### Manual Testing
Use the provided `Request.http` file with your IDE or import the Postman collection.

### Health Check
```bash
curl http://localhost:8080/api/v1/health
```

## 🔧 Configuration

### Application Properties
Located in `src/main/resources/application.yml`:

- **Server Port**: 8080
- **Database**: H2 in-memory
- **Logging**: DEBUG level for development

### Environment Variables
- `SPRING_PROFILES_ACTIVE`: Set active profile
- `SERVER_PORT`: Override server port

## 📝 Response Format

All API responses follow a consistent format:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "timestamp": "2024-01-15T14:30:00"
}
```

## 🚨 Error Handling

The API provides meaningful error responses:

- **400 Bad Request**: Validation errors
- **404 Not Found**: Resource not found
- **409 Conflict**: Duplicate resources
- **500 Internal Server Error**: Server errors

## 🎯 Core Spring Boot Concepts Demonstrated

1. **Spring IoC Container**: Automatic bean management
2. **Dependency Injection**: Constructor-based injection
3. **Spring MVC**: REST controller implementation
4. **Spring Data JPA**: Repository pattern
5. **Bean Validation**: Input validation
6. **Exception Handling**: Global exception handlers
7. **Auto Configuration**: Spring Boot magic

## 📋 Project Structure

```
src/
├── main/
│   ├── java/com/cloudnova/taskmanagementapi/
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── model/               # Entities
│   │   ├── dto/                 # Data transfer objects
│   │   ├── exception/           # Exception handling
│   │   └── TaskManagementApiApplication.java
│   └── resources/
│       └── application.yml      # Configuration
├── test/                        # Test classes
├── Dockerfile                   # Docker configuration
├── docker-compose.yml           # Docker Compose setup
└── pom.xml                     # Maven dependencies
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For questions or issues, please create an issue in the repository or contact <Abdul Baaki Hudu>.

---

**Happy Coding! 🚀**