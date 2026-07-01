# Budgeting API

A RESTful API built with Java Spring Boot, implementing JWT-based authentication, user registration, and secure login.

## Tech Stack
- Java Spring Boot
- Spring Security with JWT (HS256)
- BCrypt password hashing
- PostgreSQL with JPA/Hibernate
- Input validation and global exception handling

## Endpoints
- `POST /auth/register` — Register a new user
- `POST /auth/login` — Authenticate and receive a JWT token
- `GET /health` — Health check

## Notes
Expense tracking data model is scaffolded but not implemented.
This project is not actively maintained.
