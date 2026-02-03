# Secure Echos API Plus

## 1. Project Title & Short Description

**Secure Echos API Plus** is a production-ready Java Spring Boot backend application that provides **secure user authentication, encrypted data/file storage (vault), and controlled access APIs**. The system is designed to demonstrate best practices in **Spring Boot 3, JWT-based security, AES encryption, RESTful API design, and PostgreSQL persistence**.

This project is suitable for learning, extending, and deploying a real-world secure backend service.

---

## 2. Problem Statement & Motivation

Modern applications must securely handle sensitive user data and files while exposing APIs that are authenticated, authorized, and auditable.

**Problems addressed:**

* Secure user authentication and authorization
* Protecting sensitive data using encryption
* Preventing unauthorized access to stored resources
* Clean separation of concerns and maintainable architecture

**Motivation:**
This project serves as:

* A reference implementation for secure Spring Boot APIs
* A foundation for vault-like applications (password managers, document lockers, etc.)
* A backend suitable for enterprise-grade extension and cloud deployment

---

## 3. Features

### Functional Features

* User registration and authentication
* JWT-based stateless security
* Encrypted data storage using AES
* Secure vault APIs
* File upload and management
* Role-based access control

### Non-Functional Features

* Layered architecture
* Centralized exception handling
* Input validation
* Production-ready configuration
* Docker support

---

## 4. Tech Stack

| Layer            | Technology            | Version    |
| ---------------- | --------------------- | ---------- |
| Language         | Java                  | 17         |
| Framework        | Spring Boot           | 3.5.6      |
| Security         | Spring Security + JWT | jjwt 0.9.1 |
| Database         | PostgreSQL            | 14+        |
| ORM              | Spring Data JPA       | 3.5.6      |
| Validation       | Hibernate Validator   | Built-in   |
| Build Tool       | Maven                 | 3.9+       |
| Containerization | Docker                | Latest     |

---

## 5. System Architecture

### High-Level Architecture

```
Client → REST Controllers → Service Layer → Repository Layer → Database
                     ↓
               Security Filters (JWT)
```

### Layered Architecture

* **Controller Layer**: Handles HTTP requests and responses
* **Service Layer**: Business logic and encryption handling
* **Repository Layer**: Database interactions via JPA
* **Security Layer**: JWT filters, authentication providers

### Request–Response Flow

1. Client sends HTTP request with JWT token
2. Security filter validates token
3. Controller processes request
4. Service applies business logic and encryption
5. Repository persists or fetches data
6. Response returned to client

---

## 6. Project Structure

```
secure-echos-api-plus-main
│── src/main/java/com/devIntern/eslite
│   ├── controller        # REST controllers
│   ├── service           # Business logic
│   ├── repository        # JPA repositories
│   ├── model             # Entity classes
│   ├── payload           # DTOs & request/response models
│   ├── Security          # JWT utilities & filters
│   ├── securityConfiguration # Spring Security config
│   ├── AESUtil           # AES encryption utilities
│   ├── Exceptions        # Custom exceptions
│── src/main/resources
│   ├── application.prpperties
│── Dockerfile
│── pom.xml
```

---

## 7. Prerequisites

* Java JDK 17
* Maven 3.9+
* PostgreSQL 14+
* Docker (optional)
* Git

---

## 8. Environment Configuration

### application.properties

```properties
spring.application.name=eslite


spring.datasource.url=${DATASOURCE_URL}
spring.datasource.username=${DATASOURCE_USER}
spring.datasource.password=${DATASOURCE_PASSWORD}

# SMTP Gmail settings
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSKEY}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Optional - for debugging
#spring.mail.properties.mail.debug=true


spring.jpa.hibernate.ddl-auto=update
#spring.jpa.show-sql=true
#spring.datasource.hikari.auto-commit=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.datasource.hikari.auto-commit=false

logging.level.org.springframework.security=DEBUG

app.jwt-secret=${JWT_SECRET_KEY}
app.jwt-expiration-milliseconds=${JWT_TOKEN_EXP}

frontend.url=${FRONTEND_URL}
domain.url=${DOMAIN_URL}

max.size=${MAX_SIZE}

spring.servlet.multipart.max-file-size=25MB
spring.servlet.multipart.max-request-size=25MB
spring.jpa.show-sql=true



```

### Environment Variables

```
DATASOURCE_URL="jdbc:postgresql:/"
DATASOURCE_USER=""
DATASOURCE_PASSWORD=""
FRONTEND_URL=""
MAX_SIZE=2
JWT_SECRET_KEY=""
JWT_TOKEN_EXP=6048000
FRONTEND_URL=""
MAIL_HOST=
MAIL_PORT=
MAIL_USERNAME=""
MAIL_PASSKEY=""
DOMAIN_URL=""
```
---

## 9. Database Design

### Database Type

* PostgreSQL (Relational)

### ER Diagram (Textual)

* **User** (id, username, password, role)
* **VaultItem** (id, encryptedData, user_id)
* **File** (id, filename, encryptedContent, user_id)

**Relationships:**

* One User → Many VaultItems
* One User → Many Files

---

## 10. API Documentation


| Controller | HTTP Method | API Route | Description |
|-----------|------------|-----------|-------------|
| AuthController | POST | /api/auth/sign-in | Authenticate user and return JWT |
| AuthController | POST | /api/auth/sign-up | Register a new user |
| AuthController | GET | /api/auth/verify | Verify user account |
| AuthController | POST | /api/auth/resend-verification | Resend verification email |
| AccountController | DELETE | /api/account/delete | Delete authenticated user account |
| VaultController | POST | /api/vault/create-vault | Create a new vault |
| VaultController | POST | /api/vault/get-data | Retrieve encrypted vault data |
| VaultController | PUT | /api/vault/store-data | Store encrypted data in vault |
| VaultController | DELETE | /api/vault/delete-vault | Delete a vault |
| UploadFilesController | POST | /api/vault/file/upload/file | Upload and encrypt a file |
| UploadFilesController | GET | /api/vault/file/get-file | Download decrypted file |
| UploadFilesController | DELETE | /api/vault/file/delete | Delete stored file |


### Status Codes

* 200 OK
* 201 Created
* 400 Bad Request
* 401 Unauthorized
* 403 Forbidden
* 500 Internal Server Error

---

## 11. Security

* JWT-based authentication
* Stateless session management
* Password hashing (BCrypt – assumed)
* AES encryption for stored data

---

## 12. Exception Handling & Logging

* Global exception handler using `@ControllerAdvice`
* Custom exceptions for auth and validation
* SLF4J logging

---

## 13. Validation & Error Responses

* Bean validation annotations (`@NotNull`, `@Size`)
* Standardized error JSON responses

---

## 14. Build & Run Instructions

### Local Setup

```bash
mvn clean install
mvn spring-boot:run
```

### Docker

```bash
docker build -t secure-echos .
docker run -p 8080:8080 secure-echos
```

---

## 15. Testing

* Unit tests using JUnit 5
* Security tests using spring-security-test

```bash
mvn test
```

---

## 16. Performance Considerations

* Stateless JWT authentication
* Connection pooling via HikariCP
* Optimized JPA queries

---

## 17. Scalability & Future Enhancements

* Refresh tokens
* OAuth2 integration
* Rate limiting
* Audit logging
* Microservices split

---

## 18. Deployment Guide

### Cloud (AWS Example)

* Build Docker image
* Push to ECR
* Deploy via ECS or EC2

---

## 19. CI/CD Pipeline

* Maven build
* Unit tests
* Docker image build
* Deployment (GitHub Actions – assumed)

---

## 20. Troubleshooting & Common Issues

* JWT expiration → re-login
* DB connection issues → check credentials
* Port conflicts → change server.port

---

## 21. Contributing Guidelines

1. Fork repository
2. Create feature branch
3. Commit with clear messages
4. Open pull request

---


## 🧾 License

This project is licensed under the **MIT License** — feel free to use and modify.

---

## 👨‍💻 Author

**Ashish Kumar**  
Security-focused Java Developer  
📧 ashishchaudhary9065@gmail.com  
🌐 [LinkedIn](https://www.linkedin.com/in/ashish-kumar-0333b8373) • [GitHub](https://github.com/ashishchaudhary9523)

---

> “Security is not a feature — it’s a mindset.”