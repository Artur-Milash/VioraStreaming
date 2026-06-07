# Backend Architecture — Mail Module

## Overview

Модуль `mail` відповідає за:

* email notification delivery;
* email template rendering;
* verification email generation;
* HTML mail composition;
* mail abstraction layer.

Архітектура побудована через template-driven email system із separation between:

* message modeling;
* template rendering;
* mail transport;
* contextual data injection.

---

# Architecture Structure

## Main Functional Areas

| Area                   | Purpose                     |
| ---------------------- | --------------------------- |
| Email Rendering        | HTML template processing    |
| Message Modeling       | Typed email messages        |
| Mail Transport         | SMTP/SendGrid delivery      |
| Context Injection      | Dynamic variables           |
| Verification Messaging | Account confirmation emails |

---

# 1. Message Abstraction Layer

---

# `EmailMessage`

Base abstract class for all email message types.

---

# Purpose

Provides:

* common message contract;
* context processing;
* validation of template context;
* reusable email structure.

---

# Core Responsibilities

| Responsibility              |
| --------------------------- |
| Template context generation |
| Validation of email context |
| Message abstraction         |
| Reusable mail contract      |

---

# Main Flow

```java id="r2k9mq"
processMailMessage()
```

---

# Processing Logic

1. Generate template context
2. Validate context
3. Return Thymeleaf context
4. Pass to rendering engine

---

# Null Safety

```java id="x6n4ut"
if (ctx == null)
```

---

# Result

Prevents:

* invalid mail rendering;
* missing template variables;
* runtime rendering failures.

---

# Template Strategy

Uses constructor injection:

```java id="m5p8ac"
private final String template;
```

---

# Architectural Benefit

Enables:

* multiple email types;
* reusable rendering infrastructure;
* extensible template hierarchy.

---

# 2. Verification Email Implementation

---

# `VerifyEmailMessage`

Concrete email implementation for account verification.

---

# Purpose

Generates:

* email confirmation messages;
* verification actions;
* password recovery actions.

---

# Main Fields

| Field      | Purpose           |
| ---------- | ----------------- |
| actionUrl  | Verification link |
| actionText | CTA button text   |

---

# Default Action Strategy

```java id="q4t7hw"
DEFAULT_ACTION_TEXT = "Confirm"
```

---

# Template Variables

Injected into Thymeleaf context:

| Variable   | Purpose             |
| ---------- | ------------------- |
| actionUrl  | User redirect URL   |
| actionText | Button/action label |

---

# Context Generation

```java id="f8j2ra"
ctx.setVariable(...)
```

---

# Use Cases

| Scenario           |
| ------------------ |
| Email verification |
| Password reset     |
| Confirmation flows |
| Account activation |

---

# 3. Mail Delivery Layer

---

# `SendGridMailService`

Primary mail transport service.

---

# Responsibilities

* SMTP mail sending;
* HTML email rendering;
* template processing;
* MIME message creation.

---

# Main Dependencies

| Dependency          | Purpose             |
| ------------------- | ------------------- |
| `JavaMailSender`    | Mail transport      |
| `TemplateEngine`    | Thymeleaf rendering |
| `MimeMessageHelper` | MIME composition    |

---

# Mail Sending Flow

1. Create MIME message
2. Configure sender/receiver
3. Render HTML template
4. Attach rendered content
5. Send email

---

# MIME Composition

Uses:

```java id="v1m9qs"
MimeMessageHelper
```

---

# HTML Rendering

```java id="k8p3fd"
engine.process(...)
```

---

# Rendering Engine

Powered by:

* Thymeleaf;
* HTML templates;
* dynamic variable injection.

---

# Email Template Path

```java id="d3x5ua"
emails/base-mail-layout
```

---

# Email Characteristics

| Characteristic        | Description               |
| --------------------- | ------------------------- |
| HTML email            | Rich email support        |
| Dynamic content       | Context-driven rendering  |
| Reusable layout       | Shared template structure |
| Centralized rendering | Single rendering pipeline |

---

# Sender Configuration

Configured via:

```java id="s9w6bt"
${spring.mail.sender.app}
```

---

# Result

Supports:

* environment-based configuration;
* externalized mail settings;
* deployment flexibility.

---

# 4. Template Rendering Architecture

---

# Thymeleaf Integration

Mail rendering integrates with:

* Spring Template Engine;
* HTML templates;
* dynamic context binding.

---

# Rendering Pipeline

```text id="u7c4ne"
EmailMessage
    ↓
Context generation
    ↓
Thymeleaf rendering
    ↓
HTML output
    ↓
SMTP delivery
```

---

# Benefits

| Benefit                       |
| ----------------------------- |
| Clean separation of templates |
| Reusable layouts              |
| Dynamic email variables       |
| Maintainable email system     |

---

# 5. Integration with Other Modules

---

# Account Module Integration

Used by:

* registration flows;
* account activation;
* password recovery.

---

# Verification Module Integration

Supports:

* verification token delivery;
* confirmation workflows;
* secure user validation.

---

# Security Integration

Mail system participates in:

* secure verification flows;
* password reset operations;
* token-based actions.

---

# Example Flow

## Registration Verification

1. User registers
2. Verification token generated
3. `VerifyEmailMessage` created
4. HTML template rendered
5. Email delivered

---

# Password Recovery Flow

1. User requests password reset
2. Recovery token generated
3. Email action URL injected
4. Mail sent
5. User confirms reset

---

# 6. Main Data Flows

---

# Verification Email Flow

1. Generate verification token
2. Build verification URL
3. Create `VerifyEmailMessage`
4. Generate Thymeleaf context
5. Render HTML template
6. Send via SMTP

---

# Template Rendering Flow

1. Message object created
2. `getContext()` executed
3. Variables injected
4. Thymeleaf processes template
5. HTML output generated

---

# SMTP Delivery Flow

1. Build MIME message
2. Configure metadata
3. Attach rendered HTML
4. Send using `JavaMailSender`

---

# 7. Key RTM Modules / References

---

## Message Models

* `mail/messages/EmailMessage`
* `mail/messages/VerifyEmailMessage`

---

## Services

* `mail/services/SendGridMailService`

---

## External Dependencies

* `JavaMailSender`
* `TemplateEngine`
* `MimeMessageHelper`

---

## Related Configurations

* `configs/ThymeleafConfigs`

---

## Related Business Modules

* `account`
* `verification`
* `auth`
