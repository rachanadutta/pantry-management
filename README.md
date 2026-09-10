# Pantry Management System

A secure backend REST API for managing pantry items, categories, and storage locations with user-specific access and authentication.

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT
- Google OAuth2
- Spring Data JPA / Hibernate
- MySQL
- Flyway
- Maven

## Features

- User registration and login
- JWT access and refresh token authentication
- Google OAuth2 login
- Secure logout and refresh token rotation
- CRUD operations for pantry items
- Predefined and custom categories
- Predefined and user-owned storage locations
- User-specific resource ownership and authorization
- Centralized exception handling
- Database schema management with Flyway

## Architecture

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

The application uses a layered architecture with DTOs, JPA entities, repositories, and services.

## Database Relationships

```text
User
 ├── Pantry Items
 └── Custom Storage Locations

Pantry Item
 ├── Category
 └── Storage Location
```

## Running Locally

### Prerequisites

- Java 21
- Maven
- MySQL

### Clone

```bash
git clone https://github.com/rachanadutta/pantry-management.git
cd pantry-management
```

### Configure

Configure your MySQL database and required application properties in your local configuration.

Do not commit passwords, API keys, or OAuth credentials.

### Run

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

Flyway automatically applies the database migrations on startup.

## API

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/logout
```

### Pantry Items

```text
GET    /api/pantry
POST   /api/pantry
GET    /api/pantry/{id}
PUT    /api/pantry/{id}
DELETE /api/pantry/{id}
```

### Categories

```text
GET /api/categories
```

### Storage Locations

```text
GET  /api/storage-locations
POST /api/storage-locations
```

## Future Improvements

- Search, sorting, and filtering
- Pagination
- Automated tests
- Swagger/OpenAPI documentation
- Docker deployment

## Author

**Rachana Dutta**

[GitHub](https://github.com/rachanadutta)
