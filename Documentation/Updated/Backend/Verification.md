# Backend Architecture — Verification Module

## Загальна архітектура

Модуль `verification` реалізує систему перевірки користувачів на основі **strategy pattern + JWT token verification flow**.

Основні сценарії:

* підтвердження email (реєстрація акаунта);
* підтвердження дії "drop password";
* генерація та валідація verification token;
* відправка email через mail subsystem;
* виконання callback-логіки після валідації.

Архітектура побудована у вигляді:

```
Controller → Service → Strategy → TokenIssuer / MailService / Domain Services
```

---

# 1. Controller Layer

## `AccountsVerificationController`

Шлях:
`org.viora.viorastreamingcore.verification.controller.AccountsVerificationController`

Відповідає за HTTP entry-point для verification flow.

---

## Endpoints

### 1. Email verification (register)

```http id="ver1"
GET /api/v1/verification/accounts/register?token={token}
```

### Логіка:

* викликає `VerificationService.verify(VERIFY_EMAIL)`
* активує акаунт
* редіректить користувача на client callback URL

```java id="ver2"
verificationService.verify(VerificationType.VERIFY_EMAIL, token);
return "redirect:" + callback;
```

---

### 2. Drop password verification

```http id="ver3"
GET /api/v1/verification/accounts/drop-password?token={token}
```

### Логіка:

* перевіряє token типу `VERIFY_DROP_PASSWORD`
* встановлює secure HTTP-only cookie
* редіректить користувача на frontend callback

```java id="ver4"
Cookie cookie = new Cookie(DROP_PASSWORD_TOKEN, token);
cookie.setHttpOnly(true);
cookie.setSecure(true);
cookie.setPath("/");
cookie.setMaxAge(15 * 60);
```

---

# 2. Service Layer

## `StrategyVerificationService`

Шлях:
`org.viora.viorastreamingcore.verification.service.impl.StrategyVerificationService`

Центральний orchestrator verification flow.

### Відповідальність:

* вибір strategy по `VerificationType`;
* делегування send/verify операцій;
* підтримка множинних стратегій на один тип.

---

## Flow:

```text id="ver5"
Controller
  ↓
StrategyVerificationService
  ↓
VerificationStrategy (list per type)
```

---

# 3. Strategy Pattern Layer

## `VerificationStrategy`

Інтерфейс реалізації різних verification сценаріїв.

---

## `DefaultVerificationStrategy`

Базова абстракція для всіх стратегій.

### Відповідальність:

* перевірка чи strategy підтримує тип;
* відправка email через MailService;
* базовий verify contract;
* генерація email message.

---

## Основні методи:

* `sendVerification(AccountDto)`
* `verify(String token)`
* `verify(String token, Consumer<Object> callback)`
* `canVerify(VerificationType)`

---

# 4. Email Verification Flow

## `EmailVerificationStrategy`

Шлях:
`org.viora.viorastreamingcore.verification.service.impl.EmailVerificationStrategy`

### Призначення:

Обробка VERIFY_EMAIL flow.

---

## Verify logic

```java id="ver6"
String email = tokenIssuer.validateAndGetEmailFromToken(token, EMAIL_TOKEN_CLAIMS);
enableAccountUseCase.enableAccount(email);
```

### Behavior:

* валідує JWT token;
* перевіряє claim `verify_email=true`;
* активує акаунт через `EnableAccountUseCase`.

---

## Token claims

```java id="ver7"
Map.of("verify_email", "true")
```

---

# 5. Drop Password Flow

## `DropPasswordVerificationStrategy`

Шлях:
`org.viora.viorastreamingcore.verification.service.impl.DropPasswordVerificationStrategy`

### Призначення:

Обробка VERIFY_DROP_PASSWORD flow.

---

## Verify logic

```java id="ver8"
String email = tokenIssuer.validateAndGetEmailFromToken(token, DROP_PASSWORD_TOKEN_CLAIMS);
callback.accept(email);
```

### Behavior:

* перевіряє JWT claim `drop_password=true`;
* повертає email через callback;
* не змінює стан акаунта напряму;
* використовується для подальших security flows.

---

## Cookie-based flow

Контролер додає:

```text id="ver9"
DROP_PASSWORD_TOKEN cookie (15 min, HttpOnly, Secure)
```

---

# 6. Token Management Layer

## `VerificationTokenIssuer`

Абстракція для JWT verification tokens.

---

## Implementation: `JwtVerificationTokenIssuer`

### Використання:

* `JwtEncoder` — створення токена;
* `JwtDecoder` — валідація токена.

---

## Token structure:

```text id="ver10"
issuer: self
subject: user email
expires: 1 hour
claims: custom verification flags
```

---

## Token issuing:

```java id="ver11"
JwtClaimsSet.builder()
  .subject(accountDto.email())
  .expiresAt(now.plus(1, ChronoUnit.HOURS))
  .claims(cl -> cl.putAll(claimsMap))
```

---

## Validation logic:

* декодує JWT;
* перевіряє custom claims;
* повертає email (subject);

---

# 7. Strategy Configuration Layer

## `VerificationStrategyConfigs`

Шлях:
`org.viora.viorastreamingcore.verification.configs.VerificationStrategyConfigs`

---

## Role:

Створює runtime mapping:

```text id="ver12"
VerificationType → List<VerificationStrategy>
```

---

## Logic:

```java id="ver13"
strategies.stream()
  .filter(s -> s.canVerify(type))
  .toList();
```

---

## Effect:

* дозволяє multiple strategies per type;
* supports extensibility (plug-in strategies);
* decouples service from concrete implementations.

---

# 8. Verification Types

## Enum:

```java id="ver14"
VERIFY_EMAIL
VERIFY_DROP_PASSWORD
```

---

# 9. Architecture Flow

## Email verification

```text id="ver15"
User clicks link
  ↓
AccountsVerificationController
  ↓
StrategyVerificationService
  ↓
EmailVerificationStrategy
  ↓
JWT validation
  ↓
EnableAccountUseCase
```

---

## Drop password verification

```text id="ver16"
User clicks link
  ↓
Controller
  ↓
DropPasswordVerificationStrategy
  ↓
JWT validation
  ↓
Callback(email)
  ↓
Cookie set (frontend flow)
```

---

# 10. Key Modules for RTM

## Verification Module

```text id="ver17"
org.viora.viorastreamingcore.verification
```

### Core components:

| Component                        | Responsibility          |
| -------------------------------- | ----------------------- |
| AccountsVerificationController   | HTTP entry point        |
| StrategyVerificationService      | orchestration layer     |
| VerificationStrategy             | strategy contract       |
| DefaultVerificationStrategy      | base implementation     |
| EmailVerificationStrategy        | email activation flow   |
| DropPasswordVerificationStrategy | password reset flow     |
| JwtVerificationTokenIssuer       | JWT creation/validation |
| VerificationStrategyConfigs      | strategy registry       |

---

## Related modules

### Account Module

```text id="ver18"
org.viora.viorastreamingcore.account
```

Used for:

* enabling accounts
* account DTO representation

---

### Mail Module

```text id="ver19"
org.viora.viorastreamingcore.mail
```

Used for:

* sending verification emails
* email message abstraction

---

### Security Module

```text id="ver20"
org.viora.viorastreamingcore.configs.security
```

Used for:

* secure cookies
* authentication context integration

---

# 11. Architectural Patterns

* Strategy Pattern (verification flows)
* Factory-like configuration mapping
* JWT-based stateless verification
* Event-driven callback style (drop-password flow)
* Layered architecture (Controller → Service → Strategy → Infrastructure)
