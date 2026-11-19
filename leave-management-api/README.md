# Leave Management REST API

## 1. Project Overview

The Leave Management REST API is a Spring Boot application developed to manage employee leave requests.

The application provides RESTful endpoints that allow users to create new leave requests, retrieve all leave requests, view individual requests, update existing requests and delete requests.

It follows a layered architecture consisting of Controller, Service and Repository layers to separate request handling, business logic and data management.

The application uses Data Transfer Objects (DTOs) for request and response handling, Bean Validation to validate incoming data and a global exception handler to manage errors.

Leave requests are stored in an in-memory `ConcurrentHashMap` repository, so no external database is required.

## 2. Build and Run Instructions

**Build the project:**

```bash
./mvnw clean package
```

**Run the application with the dev profile:**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The application starts on port `8080`.

Base URL: `http://localhost:8080`

## 3. API Endpoint Summary

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/leaves` | Retrieve all leave requests |
| GET | `/leaves/{id}` | Retrieve a leave request by ID |
| POST | `/leaves` | Create a new leave request |
| PUT | `/leaves/{id}` | Update an existing leave request |
| DELETE | `/leaves/{id}` | Delete a leave request |

## 4. Testing Instructions

The project includes automated tests to verify the functionality of the service layer and REST API endpoints.

- **Service-layer unit tests:** Verify business logic, leave request creation, retrieval, updating, deletion and exception scenarios using JUnit and Mockito.
- **REST controller tests:** Verify API endpoints, HTTP response statuses, request handling and validation using MockMvc.
- **Validation tests:** Verify that invalid request data is rejected with appropriate HTTP error responses.
- **Exception handling tests:** Verify that missing leave requests are handled with the expected HTTP 404 response.

**Run the complete test suite:**

```bash
./mvnw clean test
```

**Expected result:**

- Total tests: 19
- Failures: 0
- Errors: 0
- Skipped: 0
- Build status: BUILD SUCCESS