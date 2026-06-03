# Backend Architecture - Accounts Module

## Overview

Модуль `accounts` у backend-сервісі відповідає за:

* реєстрацію користувачів;
* управління профілем акаунта;
* відновлення паролю;
* отримання інформації про поточного користувача;
* email verification flow;
* інтеграцію із Spring Security.

Архітектура побудована за принципами layered architecture із розділенням на:

* Controller layer
* Service / Use Case layer
* Repository layer
* DTO layer
* Persistence layer (JPA entities)
* Security integration
* Verification subsystem

---

# Architecture Structure

## 1. Controller Layer

Контролери реалізують REST API та делегують бізнес-логіку у UseCase-сервіси.

### Основні контролери

### `AccountsController`

Відповідає за:

* реєстрацію;
* отримання профілю;
* оновлення профілю;
* видалення акаунта.

#### Endpoints

| Method | Endpoint                    | Description                 |
| ------ | --------------------------- | --------------------------- |
| POST   | `/api/v1/accounts/register` | Реєстрація користувача      |
| GET    | `/api/v1/accounts`          | Отримання поточного акаунта |
| PATCH  | `/api/v1/accounts`          | Оновлення профілю           |
| DELETE | `/api/v1/accounts`          | Видалення акаунта           |

### `DropPasswordController`

Відповідає за flow відновлення паролю.

#### Endpoints

| Method | Endpoint                         | Description                  |
| ------ | -------------------------------- | ---------------------------- |
| GET    | `/api/v1/accounts/drop-password` | Ініціація reset password     |
| POST   | `/api/v1/accounts/drop-password` | Підтвердження reset password |

---

# 2. Service / Use Case Layer

Сервісний шар реалізує бізнес-логіку системи.

## `UserManagementService`

### Responsibilities

* реєстрація користувача;
* update профілю;
* delete акаунта;
* password hashing;
* запуск email verification.

### Interfaces

* `RegisterUserUseCase`
* `UpdateAccountUseCase`

### Основна логіка

* перевірка існування акаунта;
* створення нового `AccountModel`;
* password encoding через `PasswordEncoder`;
* запуск verification flow через `VerificationService`.

---

## `QueryAccountService`

### Responsibilities

* завантаження акаунта для Spring Security;
* отримання поточного профілю користувача.

### Основна логіка

* інтеграція із `SecurityHelpers`;
* мапінг entity → DTO;
* lookup акаунта через repository.

---

## `DropAccountsPasswordService`

### Responsibilities

* password reset flow;
* password update;
* verification integration.

### Основна логіка

* генерація verification request;
* підтвердження reset token;
* оновлення encrypted password.

---

## `AccountAuthoritiesService`

### Responsibilities

* активація акаунта після email verification.

### Основна логіка

* пошук акаунта;
* встановлення `enabled = true`.

---

# 3. Repository Layer

## `AccountRepository`

JPA repository для роботи з таблицею `accounts`.

### Основні методи

* `existsByEmail`
* `findByEmail`
* `findById`
* `deleteById`

---

# 4. Persistence Layer

## `AccountModel`

JPA entity, що представляє таблицю `accounts`.

### Основні поля

| Field    | Description        |
| -------- | ------------------ |
| id       | Primary key        |
| email    | Email користувача  |
| password | Encoded password   |
| fullName | Повне ім’я         |
| bio      | Опис профілю       |
| enabled  | Статус верифікації |

---

# 5. DTO Layer

## DTO Objects

### `AccountDto`

Використовується для передачі публічних даних акаунта.

### `RegisterUserRequest`

Request DTO для реєстрації.

### `UpdateAccountRequest`

Request DTO для оновлення профілю.

### `DropPasswordRequest`

Request DTO для reset password flow.

---

# 6. Security Integration

## `Account`

Імплементація:

* `UserDetails`
* `CredentialsContainer`

### Responsibilities

* інтеграція зі Spring Security;
* зберігання authorities;
* очищення credentials.

### Role Model

Поточна реалізація:

* `ROLE_USER`

---

# 7. Verification Flow

Система verification інтегрована через:

## `VerificationService`

### Verification Types

* `VERIFY_EMAIL`
* `VERIFY_DROP_PASSWORD`

### Використання

* email confirmation;
* password recovery;
* token-based verification.

---

# 8. Error Handling

Використовуються кастомні exception-класи:

| Exception                         | Description             |
| --------------------------------- | ----------------------- |
| `AccountAlreadyExistsException`   | Акаунт вже існує        |
| `AccountNotFoundException`        | Акаунт не знайдено      |
| `AccountAlreadyVerifiedException` | Акаунт вже підтверджено |
| `AccountNotVerifiedException`     | Акаунт не підтверджено  |

Exceptions базуються на:

* `EntityConflictException`
* `EntityNotFoundException`
* `AccountNotAllowedException`

---

# 9. Main Data Flow

## Registration Flow

1. Client → `/register`
2. `AccountsController`
3. `UserManagementService`
4. `AccountRepository`
5. `VerificationService`
6. Email verification
7. `AccountAuthoritiesService.enableAccount()`

---

## Password Reset Flow

1. Client → `/drop-password`
2. `DropAccountsPasswordService.dropPassword()`
3. `VerificationService.sendVerification()`
4. Token verification
5. `updatePassword()`

---

# 10. Key RTM Modules / References

## Core API Controllers

* `account/controller/AccountsController`
* `account/controller/DropPasswordController`

## Business Logic

* `account/service/impl/UserManagementService`
* `account/service/impl/QueryAccountService`
* `account/service/impl/DropAccountsPasswordService`
* `account/service/impl/AccountAuthoritiesService`

## Persistence

* `account/model/AccountModel`
* `account/repository/AccountRepository`

## Security

* `account/dto/Account`
* `configs/security/SecurityHelpers`

## Verification

* `verification/service/VerificationService`
* `verification/dto/VerificationType`

## DTO Contracts

* `account/dto/AccountDto`
* `account/dto/RegisterUserRequest`
* `account/dto/UpdateAccountRequest`
* `account/dto/DropPasswordRequest`