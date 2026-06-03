# Backend Architecture — AI Module

## Overview

Модуль `ai` відповідає за AI-driven функціональність платформи:

* AI рекомендації фільмів за настроєм;
* AI discussion/chat для конкретного фільму;
* управління історією діалогів;
* інтеграцію зі Spring AI / LLM;
* персоналізацію рекомендацій на основі watch history.

Архітектура реалізована у вигляді layered architecture із чітким розділенням:

* REST Controllers
* AI Services
* Persistence Layer
* DTO Layer
* Security Integration
* AI Prompt Orchestration

---

# Architecture Structure

# 1. Controller Layer

Контролери реалізують REST API для AI subsystem.

---

## `MoodMovieController`

Відповідає за AI movie recommendations based on user mood.

### Endpoint

| Method | Endpoint                 | Description                         |
| ------ | ------------------------ | ----------------------------------- |
| POST   | `/api/v1/ai/mood-movies` | AI рекомендації фільмів за настроєм |

### Flow

1. Client надсилає mood request.
2. Controller викликає `MoodMovieService`.
3. AI генерує recommendation list.
4. Повертається список `MoodMovieSuggestion`.

---

## `MovieDiscussionController`

Відповідає за AI movie discussion/chat functionality.

### Endpoints

| Method | Endpoint                               | Description                        |
| ------ | -------------------------------------- | ---------------------------------- |
| POST   | `/api/v1/ai/discussions`               | Створення або отримання discussion |
| GET    | `/api/v1/ai/discussions/{id}/messages` | Отримання history повідомлень      |
| POST   | `/api/v1/ai/discussions/{id}/messages` | Надсилання повідомлення AI         |

### Основні можливості

* persistent AI conversations;
* ownership validation;
* movie-context discussions;
* message history reconstruction.

---

## `TestController`

Тестовий AI endpoint для локальної перевірки інтеграції зі Spring AI.

### Endpoint

| Method | Endpoint       | Description               |
| ------ | -------------- | ------------------------- |
| GET    | `/api/v1/test` | Тестування AI prompt flow |

### Purpose

* integration testing;
* prompt engineering experiments;
* AI sandbox environment.

---

# 2. AI Service Layer

---

# `MoodMovieService`

## Responsibilities

* mood-based movie recommendation;
* AI prompt generation;
* watch-history personalization;
* movie catalog preprocessing;
* AI response mapping.

---

## Main Flow

### Recommendation Pipeline

1. Отримання movie catalog через `MovieService`
2. Отримання watch history користувача
3. Формування catalog snippet
4. Побудова AI system prompt
5. Виклик LLM через `ChatClient`
6. Parsing structured AI response
7. Mapping → `MoodMovieSuggestion`

---

## AI Context Sources

### Movie Catalog

Використовується:

* title;
* genres;
* shortened plot.

### User Watch History

AI враховує:

* already watched movies;
* recommendation diversity;
* unseen content preference.

---

## AI Response Contract

### Internal DTO

```java
public record AiSuggestion(long movieId, int matchScore) {}
```

### Output DTO

```java
MoodMovieSuggestion(
    MovieSummary movie,
    String matchLabel
)
```

---

## Prompt Engineering

### System Prompt містить:

* user mood;
* watched movies;
* movie catalog;
* recommendation constraints.

### AI Instructions

* обрати рівно 5 фільмів;
* score 0-100;
* використовувати лише catalog entries.

---

# `MovieDiscussionService`

## Responsibilities

* discussion lifecycle management;
* AI conversation orchestration;
* message persistence;
* ownership validation;
* movie-context AI discussion.

---

## Main Features

### Discussion Creation

Метод:

```java
getOrCreateDiscussion()
```

Функціональність:

* reuse existing discussions;
* create persistent conversations;
* bind discussion to account + movie.

---

### Message History

Метод:

```java
getMessages()
```

Функціональність:

* chronological message retrieval;
* ownership protection;
* DTO mapping.

---

### AI Messaging

Метод:

```java
sendMessage()
```

Flow:

1. validate ownership;
2. save user message;
3. reconstruct conversation context;
4. build AI prompt;
5. call LLM;
6. persist AI response;
7. return response DTO.

---

## Conversation Context Reconstruction

Історія повідомлень трансформується у:

* `UserMessage`
* `AssistantMessage`

для передачі у Spring AI conversation pipeline.

---

## Movie-Aware AI

AI отримує:

* movie title;
* movie plot;
* previous messages.

### AI Responsibilities

* discussion of themes;
* plot explanation;
* character analysis;
* question answering.

---

# 3. Persistence Layer

---

# `MovieDiscussion`

Entity для persistent AI discussions.

## Relations

| Relation  | Target       |
| --------- | ------------ |
| ManyToOne | AccountModel |
| ManyToOne | Movie        |

## Main Fields

| Field     | Description   |
| --------- | ------------- |
| id        | Discussion ID |
| account   | Owner         |
| movie     | Related movie |
| createdAt | Timestamp     |

---

# `DiscussionMessage`

Entity для AI/user messages.

## Relations

| Relation  | Target          |
| --------- | --------------- |
| ManyToOne | MovieDiscussion |

## Main Fields

| Field      | Description       |
| ---------- | ----------------- |
| id         | Message ID        |
| discussion | Parent discussion |
| role       | USER / AI         |
| content    | Message text      |
| createdAt  | Timestamp         |

---

# `MessageRole`

Enum:

```java
USER,
AI
```

Використовується для:

* AI context reconstruction;
* UI rendering;
* message ownership typing.

---

# 4. Repository Layer

---

## `MovieDiscussionRepository`

### Main Methods

```java
findByAccountIdAndMovieId()
```

### Purpose

* discussion reuse;
* per-user discussion isolation.

---

## `DiscussionMessageRepository`

### Main Methods

```java
findByDiscussionIdOrderByCreatedAtAsc()
```

### Purpose

* ordered history retrieval;
* AI conversation reconstruction.

---

# 5. DTO Layer

---

## Request DTOs

| DTO                       | Purpose             |
| ------------------------- | ------------------- |
| `MoodRequest`             | mood input          |
| `CreateDiscussionRequest` | discussion creation |
| `SendMessageRequest`      | user message        |

---

## Response DTOs

| DTO                   | Purpose             |
| --------------------- | ------------------- |
| `MoodMovieSuggestion` | AI recommendation   |
| `DiscussionResponse`  | discussion metadata |
| `MessageResponse`     | chat message        |

---

# 6. AI Integration

---

# Spring AI Integration

Основна інтеграція виконується через:

```java
ChatClient
```

---

## AI Usage Patterns

### Structured Response AI

Використовується у:

* mood recommendations.

### Conversational AI

Використовується у:

* movie discussions.

---

# Prompt Architecture

## System Prompt

Визначає:

* role;
* constraints;
* allowed context;
* behavior.

## User Prompt

Передає:

* user request;
* message content;
* mood.

---

# 7. Security Integration

AI subsystem інтегрований із:

## `SecurityHelpers`

Використовується для:

* current user detection;
* ownership validation;
* personalized recommendations.

---

# Ownership Validation

Метод:

```java
verifyOwnership()
```

Захищає:

* discussions;
* message history;
* AI chat access.

---

# 8. Transaction Management

## Transactional Operations

Використовуються для:

* discussion creation;
* message persistence;
* AI response persistence.

### Methods

* `getOrCreateDiscussion()`
* `sendMessage()`

---

# 9. Main Data Flows

---

# Mood Recommendation Flow

1. User → mood request
2. Fetch catalog
3. Fetch watch history
4. Build AI context
5. LLM recommendation
6. Structured response parsing
7. Return suggestions

---

# Movie Discussion Flow

1. Create/Open discussion
2. Save user message
3. Reconstruct context
4. Generate AI response
5. Save AI message
6. Return response

---

# 10. Key RTM Modules / References

---

## Controllers

* `ai/controller/MoodMovieController`
* `ai/controller/MovieDiscussionController`
* `ai/controller/TestController`

---

## AI Services

* `ai/service/MoodMovieService`
* `ai/service/MovieDiscussionService`

---

## Persistence

* `ai/model/MovieDiscussion`
* `ai/model/DiscussionMessage`
* `ai/model/MessageRole`

---

## Repositories

* `ai/repository/MovieDiscussionRepository`
* `ai/repository/DiscussionMessageRepository`

---

## DTO Contracts

* `ai/dto/MoodRequest`
* `ai/dto/MoodMovieSuggestion`
* `ai/dto/CreateDiscussionRequest`
* `ai/dto/DiscussionResponse`
* `ai/dto/SendMessageRequest`
* `ai/dto/MessageResponse`

---

## Security

* `configs/security/SecurityHelpers`

---

## AI Integration

* `org.springframework.ai.chat.client.ChatClient`
