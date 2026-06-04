# Backend Architecture — Configs & Infrastructure Module

## Overview

Модуль `configs` відповідає за core infrastructure configuration backend-системи:

* security configuration;
* JWT authentication infrastructure;
* exception handling;
* serialization/deserialization;
* CORS policy;
* Thymeleaf templating;
* Spring Security integration;
* utility beans/configurations.

Архітектура цього шару є foundation layer для всіх backend modules.

---

# Architecture Structure

## Main Configuration Areas

| Area               | Purpose                        |
| ------------------ | ------------------------------ |
| Security           | Authentication & authorization |
| JWT                | Token generation & validation  |
| Exception Handling | Centralized API error handling |
| Serialization      | JSON mapping                   |
| Template Engine    | HTML/email rendering           |
| Utilities          | Shared infrastructure helpers  |

---

# 1. Exception Handling Layer

---

# `ApiError`

Standard API error DTO.

## Fields

| Field     | Description      |
| --------- | ---------------- |
| timeStamp | Error timestamp  |
| errorCode | HTTP status code |
| message   | Error message    |

---

# `DefaultRestExceptionHandler`

Global exception handling layer.

## Annotation

```java id="9p4j0x"
@RestControllerAdvice
```

---

## Responsibilities

* centralized exception mapping;
* validation error formatting;
* standardized error responses;
* logging integration.

---

# Supported Exception Types

| Exception                         | HTTP Status |
| --------------------------------- | ----------- |
| `EntityNotFoundException`         | 404         |
| `EntityConflictException`         | 409         |
| `MethodArgumentNotValidException` | 400         |

---

# Validation Error Handling

Validation errors трансформуються у:

```json id="x5t2vn"
{
  "field": "validation message"
}
```

---

# Logging Strategy

Використовується:

```java id="v6w0eq"
@Slf4j
```

### Logging Levels

* validation → debug;
* conflicts → debug;
* missing entities → debug.

---

# 2. Security Infrastructure

---

# `SecurityConfigs`

Основна Spring Security configuration.

---

## Main Responsibilities

* endpoint authorization;
* JWT filter registration;
* password encoding;
* CORS configuration;
* stateless security setup.

---

# Security Pipeline

## Disabled Features

```java id="rm7d1x"
csrf()
formLogin()
httpBasic()
```

### Reason

Система використовує:

* stateless JWT authentication;
* REST API architecture.

---

# Authorization Rules

## Anonymous Endpoints

| Endpoint                          |
| --------------------------------- |
| `/api/v1/accounts/register`       |
| `/api/v1/auth`                    |
| `/api/v1/accounts/drop-password`  |
| `/api/v1/verification/accounts/*` |

---

## Public Endpoints

| Endpoint        |
| --------------- |
| Swagger/OpenAPI |
| `/api/v1/test`  |
| `/actuator/*`   |

---

## Protected Endpoints

Усі інші endpoints:

```java id="hjc7lz"
authenticated()
```

---

# Password Encoding

## Bean

```java id="o0qv7m"
BCryptPasswordEncoder(10)
```

### Purpose

* secure password hashing;
* adaptive hashing cost.

---

# CORS Configuration

## Allowed Origins

```java id="4bjlwm"
http://localhost
```

---

## Allowed Methods

* GET
* POST
* PUT
* DELETE
* OPTIONS
* PATCH

---

# `JwtAuthFilter`

Custom JWT authentication filter.

## Extends

```java id="h1zq2w"
OncePerRequestFilter
```

---

## Responsibilities

* JWT extraction;
* token validation;
* user authentication;
* SecurityContext population.

---

# Authentication Flow

1. Read `Authorization` header
2. Extract Bearer token
3. Validate JWT
4. Load user details
5. Create authentication object
6. Populate `SecurityContextHolder`

---

# Security Context Integration

Uses:

```java id="2rqx8n"
UsernamePasswordAuthenticationToken
```

для встановлення authenticated principal.

---

# `JwtConfig`

JWT cryptography configuration.

---

## Main Beans

| Bean         | Purpose          |
| ------------ | ---------------- |
| `JwtEncoder` | Token signing    |
| `JwtDecoder` | Token validation |

---

# Algorithm

## JWT Signing

```java id="1m0qzj"
HS256
```

---

# Secret Management

JWT secret:

```java id="yc0r5f"
@Value("${jwt.key}")
```

---

# `JwtTokenService`

Core JWT management service.

---

## Responsibilities

* JWT generation;
* token validation;
* password reset tokens;
* claim extraction.

---

# Main Methods

| Method                               | Purpose                   |
| ------------------------------------ | ------------------------- |
| `generateToken()`                    | Access token generation   |
| `isValidToken()`                     | Token validation          |
| `generateDropPasswordToken()`        | Password reset token      |
| `getUsernameFromToken()`             | Subject extraction        |
| `getUsernameFromDropPasswordToken()` | Password reset validation |

---

# Token Types

## Access Token

### Lifetime

```java id="5u7j2p"
1 hour
```

### Claims

* issuer;
* issuedAt;
* expiresAt;
* subject.

---

## Drop Password Token

### Lifetime

```java id="e6m9tk"
15 minutes
```

### Additional Claim

```java id="w8f2as"
purpose = drop-password
```

---

# Token Validation

Validation includes:

* JWT signature validation;
* expiration validation;
* purpose validation.

---

# `SecurityHelpers`

Utility service для отримання authenticated user information.

---

## Main Method

```java id="n3k0vr"
getCurrentlyAuthenticatedAccountId()
```

---

## Purpose

* authenticated account lookup;
* service-layer identity access;
* ownership validation.

---

# `VioraUserDetailsService`

Custom Spring Security user loader.

---

## Implements

```java id="y7a2mq"
UserDetailsService
```

---

## Responsibilities

* loading users by email;
* Spring Security integration;
* delegation to account subsystem.

---

# User Loading Flow

1. Spring Security requests user
2. `GetUserAccountUseCase`
3. `Account` principal returned
4. Authentication completed

---

# 3. Template Engine Configuration

---

# `ThymeleafConfigs`

Конфігурація HTML/email templating.

---

## Main Components

| Component                        | Purpose            |
| -------------------------------- | ------------------ |
| `SpringResourceTemplateResolver` | Template loading   |
| `SpringTemplateEngine`           | Template rendering |

---

# Template Configuration

## Location

```java id="bd2wsm"
classpath:/templates/
```

---

## Template Type

```java id="c8x3qn"
HTML
```

---

## Encoding

```java id="a1p6rz"
UTF-8
```

---

# Usage

Використовується для:

* verification emails;
* password reset emails;
* dynamic HTML rendering.

---

# 4. Serialization & Utilities

---

# `UtilsConfigs`

Infrastructure utility configuration.

---

# ObjectMapper Configuration

## Registered Modules

```java id="n0d8lt"
JavaTimeModule
```

---

## Serialization Features

Disabled:

```java id="2x9vfa"
WRITE_DATES_AS_TIMESTAMPS
```

---

## Deserialization Features

Disabled:

```java id="4m6kcy"
FAIL_ON_UNKNOWN_PROPERTIES
```

---

# Purpose

* ISO date serialization;
* flexible JSON parsing;
* Java Time support.

---

# Jackson Builder Customizer

Additional Spring Boot JSON customization.

---

# 5. Main Security Data Flow

---

# Authentication Flow

1. User logs in
2. JWT generated
3. Client stores token
4. Client sends Bearer token
5. `JwtAuthFilter` validates token
6. SecurityContext populated
7. Protected endpoint accessed

---

# Password Reset Flow

1. Generate special-purpose JWT
2. Send verification email
3. Validate token purpose
4. Allow password update

---

# Exception Handling Flow

1. Exception thrown
2. `DefaultRestExceptionHandler`
3. Map exception → HTTP response
4. Return standardized error JSON

---

# 6. Key RTM Modules / References

---

## Exception Handling

* `configs/handlers/ApiError`
* `configs/handlers/DefaultRestExceptionHandler`

---

## Security Configuration

* `configs/security/SecurityConfigs`
* `configs/security/JwtAuthFilter`
* `configs/security/JwtConfig`

---

## JWT Infrastructure

* `configs/security/JwtTokenService`

---

## Security Helpers

* `configs/security/SecurityHelpers`
* `configs/security/VioraUserDetailsService`

---

## Template Engine

* `configs/ThymeleafConfigs`

---

## Serialization & Utilities

* `configs/utils/UtilsConfigs`
