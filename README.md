# Java TDD REST API

![CI](https://github.com/denzilantony/java-tdd-rest-api/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![TDD](https://img.shields.io/badge/TDD-passing-brightgreen?style=flat)
![License](https://img.shields.io/badge/License-MIT-green?style=flat)

A Task Management REST API built with **strict Test-Driven Development 
(TDD)** — tests written first, implementation second.

Built to demonstrate production-quality backend engineering practices
used in high-scale enterprise systems.

---

## TDD Approach
This project follows strict TDD discipline:
	1. Write a failing test first
	2. Write minimum code to make it pass
	3. Refactor while keeping tests green

Every feature in this codebase was driven by a test.

---

## Test Coverage
TaskServiceApplicationTests     ✅
Task Service Tests               ✅  12/12
Task Controller Tests            ✅   8/8
─────────────────────────────────────────
Total                            ✅  21/21

---

## Tech Stack
- **Java 21** + **Spring Boot 3.x**
- **JUnit 5** + **Mockito** — unit and integration tests
- **Spring MockMvc** — controller layer testing
- **PostgreSQL** (production) + **H2** (tests)
- **Docker + Docker Compose** — one command startup
- **GitHub Actions** — CI pipeline on every push

---

## API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/tasks` | Get all tasks |
| GET | `/api/v1/tasks/{id}` | Get task by ID |
| POST | `/api/v1/tasks` | Create new task |
| PUT | `/api/v1/tasks/{id}` | Update task |
| PATCH | `/api/v1/tasks/{id}/status` | Update task status |
| DELETE | `/api/v1/tasks/{id}` | Delete task |
| GET | `/api/v1/tasks/status/{status}` | Filter by status |
| GET | `/api/v1/tasks/priority/{priority}` | Filter by priority |
| GET | `/api/v1/tasks/search?title=` | Search by title |
| GET | `/api/v1/tasks/active` | Get active tasks |

---

## Task Status Flow
TODO → IN_PROGRESS → DONE
→ CANCELLED

## Priority Levels
LOW → MEDIUM → HIGH → CRITICAL

---

## Running Locally

### Start everything
```bash
docker-compose up --build
```

### Run tests
```bash
mvn test
```

### Access API
http://localhost:8080/api/v1/tasks
http://localhost:8080/actuator/health

---

## Author

**Denzil Antony** — Senior Java Backend Engineer, Germany
[linkedin.com/in/denzilantony](https://linkedin.com/in/denzilantony)