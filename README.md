# Task Manager API

A RESTful web service for managing tasks with role-based access control using JWT authentication.

## Technologies Used

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- JWT (Json Web Token)
- Maven
- Lombok
- PostgreSQL
- MapStruct

## Roles and Permissions

| Role  | Permissions                                          |
|-------|------------------------------------------------------|
| USER  | `task:read`, `task:write`                            |
| ADMIN | `task:read`, `task:write`, `user:read`, `user:write` |

## Authentication

- JWT is used for authentication and authorization.
- To access secured endpoints, obtain a token via `/api/auth/login` and include it in the `Authorization` header: Authorization: Bearer <your_token>

## API Endpoints

### Authentication ('/api/auth')

| Method | Endpoint    | Description                      |
|--------|-------------|----------------------------------|
| POST   | `/register` | Register a new user              |
| POST   | `/login`    | Authenticate and receive a token |

### Task Management ('/api/tasks')

| Method | Endpoint    | Roles                  | Description       |
|--------|-------------|------------------------|-------------------|
| GET    | `/`         | ADMIN, USER            | Get list of tasks |
| POST   | `/`         | ADMIN, USER            | Create a new task |
| PUT    | `/{userId}` | ADMIN, USER (if owner) | Update a task     |
| DELETE | `/{userId}` | ADMIN, USER (if owner) | Delete a task     |

### Admin Operations ('/api/admin')

| Method | Endpoint    | Roles | Description              |
|--------|-------------|-------|--------------------------|
| GET    | `/`         | ADMIN | Get all registered users |
| PUT    | `/{userId}` | ADMIN | Update user information  |
| DELETE | `/{userId}` | ADMIN | Delete a user            |

## Admin Registration

To register a user with **ADMIN** role, use the following endpoint:

### `POST /api/auth/admin-register`

This endpoint accepts the same fields as a regular registration request, plus `adminCode` parameter.

If the `adminCode` is correct (current value: `pass777`), the user will granter the `ADMIN` role.

If the code is incorrect or missing, the request will be rejected.

## Validation Rules

### User

| Field      | Description                                              |
|------------|----------------------------------------------------------|
| `username` | Required. Length must be between 3 and 20 characters     |
| `password` | Required. Must contain at least 8 characters (up to 100) |
| `email`    | Required. Must be a valid email address                  |
| `role`     | Optional (set automatically or by admin)                 |

### Task

| Field         | Description                                                         |
|---------------|---------------------------------------------------------------------|
| `title`       | Required. Maximum length: 100 characters                            |
| `description` | Optional. Maximum length: 1000 characters                           |
| `dueDate`     | Required. Must be a valid date (format: `YYYY-MM-DD`)               |
| `status`      | Required. Must be one of the predefined values (e.g. `IN_PROGRESS`) |
| `assignedTo`  | Must reference to the current user                                  |

## How to Run

1. Clone the repository:
```
git clone https://github.com/Dzaza375/task-manager
cd task-manager
```
2. Build the project:
```
mvn clean install
```
3. Run the application:
```
mvn spring-boot:run
```

## Notes

- All secured endpoints require a valid JWT.
- Admin users can manage all tasks and users.
- Regular users can only manage their own tasks.