#  LMS System

A simple backend REST API for managing online courses, built with Spring Boot.
This is a personal pet-project to practice Java, Spring Boot, Spring Security, and REST API design.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Main language |
| Spring Boot 3.2.5 | Framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Database access |
| PostgreSQL | Database |
| MapStruct | DTO ↔ Entity mapping |
| Lombok | Reducing boilerplate code |
| Gradle | Build tool |

---

## How to Run

### 1. Requirements
- Java 17+
- PostgreSQL running locally
- Gradle (or use `./gradlew`)

### 2. Set up the database

Create a PostgreSQL database:
```sql
CREATE DATABASE LMS_system;
```

### 3. Configure the app

Open `src/main/resources/application.properties` and set your DB credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/LMS_system
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 4. Run the app

```bash
./gradlew bootRun
```

The server starts at `http://localhost:8080`

---

## Authentication

This API uses **HTTP Basic Auth**.

First register a user, then include credentials in every request.

There are two roles:
- `ROLE_STUDENT` — can enroll in courses, view lessons
- `ROLE_TEACHER` — can create and manage courses

---

## API Endpoints

### Users

| Method | URL | Description | Auth required |
|---|---|---|---|
| POST | `/users/register` | Register a new user | No |
| GET | `/users` | Get all users | Yes |
| GET | `/users/{id}` | Get user by ID | Yes |
| DELETE | `/users/{id}` | Delete user | Yes |

**Register example:**
```json
POST /users/register
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "secret123",
  "role": "ROLE_STUDENT"
}
```

---

### Courses

> Requires `ROLE_TEACHER`

| Method | URL | Description |
|---|---|---|
| POST | `/courses` | Create a course |
| GET | `/courses` | Get all courses |
| GET | `/courses/{id}` | Get course by ID |
| DELETE | `/courses/{id}` | Delete a course |

**Create course example:**
```json
POST /courses
{
  "name": "Java Basics",
  "description": "Learn Java from scratch",
  "teacherId": 1
}
```

---

### Lessons

| Method | URL | Description |
|---|---|---|
| POST | `/lessons` | Create a lesson |
| GET | `/lessons/course/{courseId}` | Get lessons by course |
| DELETE | `/lessons/{id}` | Delete a lesson |

**Create lesson example:**
```json
POST /lessons
{
  "title": "Variables and Types",
  "content": "In Java, variables must have a type...",
  "courseId": 1
}
```

---

### Enrollments

> Only `ROLE_STUDENT` can enroll

| Method | URL | Description |
|---|---|---|
| POST | `/enrollments?studentId=1&courseId=1` | Enroll student in course |
| GET | `/enrollments/student/{studentId}` | Get courses of a student |
| GET | `/enrollments/course/{courseId}` | Get students in a course |

---

## 🗂 Project Structure

```
src/main/java/practice/lms_students/
├── Controllers/       — HTTP endpoints (REST API)
├── DTO/               — Data Transfer Objects (what client sends/receives)
├── Entity/            — Database models (JPA)
├── Mapper/            — Converts Entity ↔ DTO (MapStruct)
├── Repository/        — Database queries (Spring Data JPA)
├── Security/          — Authentication & Authorization (Spring Security)
└── Service/           — Business logic
```

### How a request flows:

```
Client
  ↓
Controller      (receives HTTP request)
  ↓
Service         (checks business rules)
  ↓
Repository      (reads/writes to DB)
  ↓
Mapper + DTO    (formats the response)
  ↓
Client          (gets clean JSON back)
```

