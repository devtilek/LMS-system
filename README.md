# LMS System

A backend REST API for managing an online learning platform. The project demonstrates layered architecture, role-based access control, DTO mapping, validation, and PostgreSQL persistence with Spring Boot.

## Features

- Student and teacher roles
- Secure registration with BCrypt password hashing
- HTTP Basic authentication
- Role-based authorization with Spring Security
- Course management for teachers
- Lesson management for teachers
- Student course enrollment
- Protection against duplicate enrollments
- DTO ↔ Entity mapping with MapStruct
- Centralized REST exception handling
- Request validation with Jakarta Validation
- PostgreSQL persistence with Spring Data JPA
- Environment-based database configuration

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Main language |
| Spring Boot 3.2.5 | Application framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA / Hibernate | Persistence |
| PostgreSQL | Database |
| MapStruct | DTO ↔ Entity mapping |
| Lombok | Boilerplate reduction |
| Gradle | Build tool |
| Jakarta Validation | Request validation |

## Architecture

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL

Entity ↔ Mapper ↔ DTO
Security → Authentication / Authorization
Exception → GlobalExceptionHandler
```

Project structure:

```text
src/main/java/practice/lms_students/
├── Controllers/
├── DTO/
├── Entity/
├── Exception/
├── Mapper/
├── Repository/
├── Security/
└── Service/
```

## Getting Started

### Requirements

- Java 17+
- PostgreSQL 14+
- Git

### Database

Create the database:

```sql
CREATE DATABASE LMS_system;
```

### Environment variables

Copy `.env.example` and configure your local environment. The application supports:

```text
DB_URL=jdbc:postgresql://localhost:5432/LMS_system
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

Do not commit real credentials to GitHub.

### Run

Linux/macOS:

```bash
./gradlew bootRun
```

Windows:

```bash
gradlew.bat bootRun
```

The API runs on `http://localhost:8080`.

## Authentication and Authorization

The API uses HTTP Basic authentication.

A public registration endpoint creates **STUDENT** accounts. The role supplied by a client during registration is intentionally ignored so that an unauthenticated user cannot create a teacher account.

Teacher accounts should be provisioned separately by an administrator/database seed until a dedicated admin API is introduced.

### Permissions

| Operation | Student | Teacher |
|---|:---:|:---:|
| Register | ✓ | ✓ |
| View courses | ✓ | ✓ |
| Create/delete courses | — | ✓ |
| View lessons | ✓ | ✓ |
| Create/delete lessons | — | ✓ |
| Enroll in a course | ✓ | — |
| View own enrollments | ✓ | — |
| View course enrollments | — | ✓ |

## API

### Register

```http
POST /users/register
Content-Type: application/json
```

```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

### Courses

```text
GET    /courses
GET    /courses/{id}
POST   /courses              # TEACHER
DELETE /courses/{id}         # TEACHER
```

Create course:

```json
{
  "name": "Java Basics",
  "description": "Learn Java from scratch"
}
```

The teacher is taken from the authenticated user rather than from a client-supplied ID.

### Lessons

```text
GET    /lessons/course/{courseId}
POST   /lessons               # TEACHER
DELETE /lessons/{id}          # TEACHER
```

Create lesson:

```json
{
  "title": "Variables and Types",
  "content": "In Java, variables must have a type...",
  "courseId": 1
}
```

### Enrollments

```text
POST /enrollments?courseId=1
GET  /enrollments/student/{studentId}
GET  /enrollments/course/{courseId}   # TEACHER
```

The student ID is derived from the authenticated account during enrollment. A student cannot enroll another user, and duplicate student/course combinations are rejected.

## Error Responses

The API returns consistent JSON errors for common failures such as validation errors, missing resources, and duplicate resources.

Example:

```json
{
  "timestamp": "2026-09-08T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Course not found"
}
```

## Development Notes

- `spring.jpa.open-in-view=false` is enabled to avoid accidental database access from the web layer.
- Relationships are lazy-loaded where appropriate.
- Lesson and enrollment filtering is performed by PostgreSQL through repository queries instead of loading every record into memory.
- Enrollment has a database-level unique constraint on `(student_id, course_id)`.

## Roadmap

- JWT authentication with refresh tokens
- Admin role and teacher management
- Pagination and sorting
- OpenAPI / Swagger documentation
- Unit and integration tests
- Docker Compose for PostgreSQL and the application
- CI pipeline with GitHub Actions
