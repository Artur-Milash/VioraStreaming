# Backend Architecture — History Module

## Overview

Модуль `history` відповідає за:

* tracking movie watch progress;
* user watch history retrieval;
* synchronization of playback state;
* persistence of last watched segments;
* integration with recommendation/AI systems.

Архітектура побудована на event-driven history tracking із separation of concerns між:

* REST API layer;
* playback tracking services;
* persistence layer;
* movie aggregation layer;
* security-aware user resolution.

---

# Architecture Structure

## Main Functional Areas

| Area              | Purpose                |
| ----------------- | ---------------------- |
| Watch History     | User movie activity    |
| Playback Progress | Last watched segment   |
| History Retrieval | User viewing history   |
| Event Processing  | Async history updates  |
| AI Integration    | Recommendation context |

---

# 1. Controller Layer

---

# `HistoryController`

REST controller для доступу до watch history.

---

## Endpoints

| Method | Endpoint                    | Description              |
| ------ | --------------------------- | ------------------------ |
| GET    | `/api/v1/history`           | Full user history        |
| GET    | `/api/v1/history/{movieId}` | Playback state for movie |

---

# Responsibilities

* authenticated history access;
* retrieval of playback progress;
* DTO response delivery;
* movie history lookup.

---

# Request Flow

## Full History

1. Authenticated request
2. Resolve current account
3. Fetch history entries
4. Aggregate movie summaries
5. Return DTO collection

---

## Single Movie History

1. Resolve authenticated account
2. Find movie progress
3. Return playback segment
4. Fallback to empty state

---

# 2. DTO Layer

---

# `HistoryDto`

Primary API projection for playback history.

---

## Fields

| Field         | Description                      |
| ------------- | -------------------------------- |
| movie         | Lightweight movie representation |
| lastWatchedAt | Playback timestamp/segment       |

---

# Integration with Content Module

Uses:

```java id="m4k8zs"
MovieSummary
```

---

# Purpose

Avoid loading full movie metadata during history operations.

---

# 3. Persistence Layer

---

# `History`

Core watch history entity.

---

# Main Fields

| Field         | Description           |
| ------------- | --------------------- |
| id            | History record ID     |
| account       | Owner account         |
| movie         | Watched movie         |
| lastWatchedAt | Last playback segment |

---

# Entity Relationships

---

## Account Relation

```java id="d9r2xp"
@ManyToOne(fetch = FetchType.LAZY)
```

### Characteristics

* immutable ownership;
* cascade delete support.

---

## Movie Relation

```java id="q5t1mv"
@ManyToOne(fetch = FetchType.LAZY)
```

---

# Database Constraints

## Foreign Key

```sql id="h2x7ac"
ON DELETE CASCADE
```

---

# Result

Deleting account automatically removes:

* watch history;
* playback states;
* related history records.

---

# 4. Repository Layer

---

# `HistoryRepository`

Persistence abstraction for history operations.

---

## Extends

```java id="w8v3kr"
JpaRepository<History, Long>
```

---

# Core Queries

---

## `findByAccountIdAndMovieId()`

### Purpose

Retrieve playback state for:

* resume watching;
* continue playback;
* sync progress.

---

## `getHistoryByAccountId()`

### Purpose

Retrieve all user history entries.

---

# Repository Responsibilities

| Responsibility           |
| ------------------------ |
| History persistence      |
| Playback lookup          |
| User history retrieval   |
| Progress synchronization |

---

# 5. Service Layer

---

# `GetHistoryService`

Read-oriented history orchestration service.

---

# Responsibilities

* history aggregation;
* movie summary resolution;
* playback retrieval;
* DTO construction.

---

# Main Methods

| Method             | Purpose                     |
| ------------------ | --------------------------- |
| `getHistory()`     | Full history retrieval      |
| `getHistoryById()` | Single movie playback state |

---

# History Aggregation Flow

---

## Step 1 — Load History

```java id="u1m5zn"
repository.getHistoryByAccountId(...)
```

---

## Step 2 — Extract Movie IDs

```java id="r7p4qe"
Set<Long> movieIds
```

---

## Step 3 — Batch Movie Lookup

```java id="c2n8fy"
movieService.getMoviesByIds(movieIds)
```

---

## Step 4 — Build Response DTOs

```java id="j9s3hk"
HistoryDto
```

---

# Optimization Strategy

Instead of loading movie data individually:

* batch retrieval is used;
* reduces N+1 problems;
* improves scalability.

---

# Playback State Retrieval

---

# `getHistoryById()`

Returns:

* current playback segment;
* or default empty state.

---

## Default Behavior

If no history exists:

```java id="t6b1qa"
new HistoryDto(summary, 0L)
```

---

# Result

Frontend can:

* resume playback safely;
* initialize player state consistently.

---

# 6. Event-Driven History Tracking

---

# `HistoryService`

Write-oriented event processing service.

---

# Architecture Style

Uses:

```java id="k4z7rn"
@EventListener
```

---

# Benefits

| Benefit             | Description                                |
| ------------------- | ------------------------------------------ |
| Decoupling          | Playback tracking separated from streaming |
| Scalability         | Async-compatible architecture              |
| Extensibility       | Additional listeners possible              |
| Clean orchestration | Reduced controller/service coupling        |

---

# `SaveHistoryCommand`

Application event for playback synchronization.

---

## Fields

| Field     | Purpose           |
| --------- | ----------------- |
| accountId | Current user      |
| movieId   | Watched movie     |
| segment   | Playback position |

---

# Save Flow

1. Playback module publishes event
2. Event listener receives command
3. Resolve account & movie
4. Find existing history
5. Update playback segment
6. Persist state

---

# Idempotent Update Logic

```java id="g3x8vo"
.orElse(new History(...))
```

---

# Result

* existing records updated;
* missing records auto-created.

---

# Playback Synchronization

## Update Logic

```java id="y7n4lu"
history.setLastWatchedAt(command.getSegment())
```

---

# Effect

Supports:

* resume watching;
* multi-session continuity;
* device synchronization.

---

# 7. Security Integration

---

# `SecurityHelpers`

History access is fully authenticated.

---

# Used For

```java id="f8q2jm"
getCurrentlyAuthenticatedAccountId()
```

---

# Security Guarantees

| Guarantee                   |
| --------------------------- |
| User-only history access    |
| Isolated playback state     |
| No cross-account visibility |

---

# 8. Integration with Other Modules

---

# Content Module Integration

Uses:

* `MovieService`;
* `MovieRepository`;
* `MovieSummary`.

---

# Account Module Integration

Uses:

* `AccountRepository`;
* authenticated account resolution.

---

# AI Module Integration

History data is reused for:

* recommendation personalization;
* watched movie filtering;
* mood-based suggestions.

---

# Example Integration

Used in:

```java id="p6m3xt"
MoodMovieService
```

to avoid recommending watched movies.

---

# 9. Main Data Flows

---

# Watch Progress Save Flow

1. User watches movie
2. Playback segment updated
3. `SaveHistoryCommand` published
4. `HistoryService` processes event
5. Database updated

---

# Resume Playback Flow

1. Client opens movie
2. Request playback state
3. Retrieve `History`
4. Return `lastWatchedAt`
5. Resume playback

---

# History Feed Flow

1. Request user history
2. Fetch history entities
3. Batch load movie summaries
4. Build DTO responses
5. Return ordered history

---

# 10. Key RTM Modules / References

---

## Controllers

* `history/controller/HistoryController`

---

## DTOs

* `history/dto/HistoryDto`

---

## Entities

* `history/model/History`

---

## Repositories

* `history/repository/HistoryRepository`

---

## Services

* `history/service/GetHistoryService`
* `history/service/HistoryService`

---

## Events

* `history/service/command/SaveHistoryCommand`

---

## External Integrations

* `content/service/MovieService`
* `content/repository/MovieRepository`
* `account/repository/AccountRepository`
* `configs/security/SecurityHelpers`
