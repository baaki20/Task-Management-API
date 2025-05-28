# Task Management API - REST Endpoints Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
Currently, no authentication is required (basic implementation).

---

## Task Endpoints

### 1. Get All Tasks
**GET** `/tasks`

**Query Parameters:**
- `status` (optional): Filter by task status (`TODO`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`)
- `search` (optional): Search tasks by title or description

**Examples:**
```bash
# Get all tasks
curl -X GET "http://localhost:8080/api/v1/tasks"

# Get tasks by status
curl -X GET "http://localhost:8080/api/v1/tasks?status=TODO"

# Search tasks
curl -X GET "http://localhost:8080/api/v1/tasks?search=urgent"
```

**Response:**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Complete project documentation",
      "description": "Write comprehensive API documentation",
      "status": "TODO",
      "statusDisplay": "To Do",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "timestamp": "2024-01-15T14:30:00"
}
```

### 2. Get Task by ID
**GET** `/tasks/{id}`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/tasks/1"
```

### 3. Create New Task
**POST** `/tasks`

**Request Body:**
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive API documentation",
  "status": "TODO"
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/api/v1/tasks" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive API documentation",
    "status": "TODO"
  }'
```

### 4. Update Task
**PUT** `/tasks/{id}`

**Request Body:**
```json
{
  "title": "Updated task title",
  "description": "Updated description",
  "status": "IN_PROGRESS"
}
```

**Example:**
```bash
curl -X PUT "http://localhost:8080/api/v1/tasks/1" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated task title",
    "description": "Updated description",
    "status": "IN_PROGRESS"
  }'
```

### 5. Delete Task
**DELETE** `/tasks/{id}`

**Example:**
```bash
curl -X DELETE "http://localhost:8080/api/v1/tasks/1"
```

### 6. Complete Task
**PATCH** `/tasks/{id}/complete`

**Example:**
```bash
curl -X PATCH "http://localhost:8080/api/v1/tasks/1/complete"
```

### 7. Get Task Statistics
**GET** `/tasks/statistics`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/tasks/statistics"
```

**Response:**
```json
{
  "success": true,
  "message": "Task statistics retrieved successfully",
  "data": {
    "todoCount": 5,
    "inProgressCount": 3,
    "completedCount": 10,
    "cancelledCount": 1,
    "totalCount": 19
  },
  "timestamp": "2024-01-15T14:30:00"
}
```

---

## Health Check Endpoints

### 1. Health Check
**GET** `/health`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/health"
```

### 2. Ping
**GET** `/health/ping`

**Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/health/ping"
```

---

## Task Status Values

| Status | Display Name | Description |
|--------|--------------|-------------|
| `TODO` | To Do | Task is created but not started |
| `IN_PROGRESS` | In Progress | Task is currently being worked on |
| `COMPLETED` | Completed | Task has been finished |
| `CANCELLED` | Cancelled | Task has been cancelled |

---

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "title": "Title is required",
    "description": "Description cannot exceed 500 characters"
  },
  "timestamp": "2024-01-15T14:30:00"
}
```

### Task Not Found (404 Not Found)
```json
{
  "success": false,
  "message": "Task not found with ID: 999",
  "data": null,
  "timestamp": "2024-01-15T14:30:00"
}
```

### Duplicate Task (409 Conflict)
```json
{
  "success": false,
  "message": "Task with title 'Existing Task' already exists",
  "data": null,
  "timestamp": "2024-01-15T14:30:00"
}
```

---

## Testing with Postman

### Environment Variables
Create these variables in Postman:
- `baseUrl`: `http://localhost:8080/api/v1`
- `taskId`: `1` (for testing specific task operations)

### Collection Structure
```
Task Management API/
├── Tasks/
│   ├── Get All Tasks
│   ├── Get Task by ID
│   ├── Create Task
│   ├── Update Task
│   ├── Delete Task
│   ├── Complete Task
│   └── Get Statistics
└── Health/
    ├── Health Check
    └── Ping
```

---

## Database Console (Development)

Access H2 Database Console at: `http://localhost:8080/api/v1/h2-console`

**Connection Settings:**
- JDBC URL: `jdbc:h2:mem:taskdb`
- User Name: `sa`
- Password: `password`