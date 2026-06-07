# Backend Architecture — Authentication Module

## Overview

Модуль `auth` відповідає за authentication flow системи:

* login/authentication;
* credential verification;
* JWT token generation;
* інтеграцію зі Spring Security;
* access control validation.

Архітектура реалізована через lightweight authentication layer із розділенням:

* REST Controller
* Authentication Service
* DTO Contracts
* Security Integration
* Exception Handling

---

# Architecture Structure

# 1. Controller Layer

---

## `AuthController`

REST controller для authentication operations.

### Endpoint

| Method | Endpoint       | Description                   |
| ------ | -------------- | ----------------------------- |
| POST   | `/api/v1/auth` | Authentication/login endpoint |

---

## Main Responsibilities

* прийом login request;
* request validation;
* delegation у authentication service;
* повернення JWT token response.

---

## Request Flow

1. Client надсилає credentials.
2. Validation через Jakarta Validation.
3. Controller викликає `LoginUserUseCase`.
4. Authentication service перевіряє credentials.
5. Генерується JWT token.
6. Token повертається клієнту.

---

# 2. DTO Layer

---

# `LoginUserRequest`

DTO для authentication request.

## Fields

| Field    | Validation            | Description   |
| -------- | --------------------- | ------------- |
| email    | `@Email`, `@NotEmpty` | User email    |
| password | `@NotEmpty`           | User password |

---

## Validation Rules

### Email

* must not be empty;
* must be valid email format.

### Password

* must not be empty.

---

# `LoginUserResponse`

DTO для authentication response.

## Fields

| Field | Description      |
| ----- | ---------------- |
| token | JWT access token |

---

# 3. Service Layer

---

# `AuthService`

Основний authentication service.

## Implements

```java id="s7k2md"
LoginUserUseCase
```

---

## Responsibilities

* user authentication;
* password verification;
* account state validation;
* JWT token generation;
* Spring Security integration.

---

# Authentication Flow

## Step 1 — User Lookup

```java id="m0e8na"
userDetailsService.loadUserByUsername(request.email())
```

### Purpose

* пошук користувача;
* integration with Spring Security;
* loading `UserDetails`.

---

## Step 2 — Password Verification

```java id="2fj2d8"
passwordEncoder.matches()
```

### Purpose

* secure password comparison;
* encoded password validation.

---

## Step 3 — Account Status Validation

```java id="zw31wx"
account.isEnabled()
```

### Purpose

* verification status check;
* block disabled accounts.

---

## Step 4 — JWT Generation

```java id="xy0d4t"
jwtTokenService.generateToken(account)
```

### Purpose

* generate signed JWT token;
* establish authenticated session.

---

# 4. Security Integration

---

# Spring Security Integration

Authentication subsystem інтегрований із:

| Component            | Purpose            |
| -------------------- | ------------------ |
| `UserDetailsService` | User loading       |
| `PasswordEncoder`    | Password hashing   |
| `UserDetails`        | Security principal |
| `JwtTokenService`    | JWT generation     |

---

# JWT Authentication

## Token Generation

JWT token генерується після:

* successful password verification;
* enabled account validation.

---

## JWT Usage

Токен використовується для:

* API authentication;
* protected endpoints;
* user identification;
* authorization context.

---

# UserDetails Integration

Authentication використовує:

```java id="r5j1ae"
UserDetails
```

що реалізується через:

```java id="d6wrmf"
account.dto.Account
```

---

# 5. Exception Handling

---

# `InvalidCredentialsException`

Викидається коли:

* password mismatch;
* invalid login credentials.

---

# `AccountDisabledException`

HTTP Status:

```java id="tp8h2w"
403 FORBIDDEN
```

Викидається коли:

* account exists;
* account is not verified/enabled.

---

# Authentication Failure Scenarios

| Scenario         | Exception                                     |
| ---------------- | --------------------------------------------- |
| Wrong password   | `InvalidCredentialsException`                 |
| Disabled account | `AccountDisabledException`                    |
| Unknown user     | `UsernameNotFoundException` (Spring Security) |

---

# 6. Main Data Flow

---

# Login Flow

1. Client → `/api/v1/auth`
2. Request validation
3. Load user via `UserDetailsService`
4. Validate password
5. Validate account status
6. Generate JWT
7. Return token

---

# Security Validation Flow

1. JWT received from client
2. JWT parsed by security filters
3. User authenticated
4. SecurityContext populated
5. Protected resources become accessible

---

# 7. Key RTM Modules / References

---

## Controllers

* `auth/controller/AuthController`

---

## Services

* `auth/service/AuthService`

---

## DTO Contracts

* `auth/dto/LoginUserRequest`
* `auth/dto/LoginUserResponse`

---

## Exceptions

* `auth/exception/InvalidCredentialsException`
* `auth/exception/AccountDisabledException`

---

## Security Infrastructure

* `configs/security/JwtTokenService`
* `org.springframework.security.core.userdetails.UserDetailsService`
* `org.springframework.security.crypto.password.PasswordEncoder`

---

## Security Domain

* `account/dto/Account`
