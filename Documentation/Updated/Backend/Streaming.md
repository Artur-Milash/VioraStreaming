# Backend Architecture — Streaming Module

## Загальна архітектура

Модуль `streaming` реалізує backend-логіку потокового відтворення відео через HLS (HTTP Live Streaming). Архітектура побудована відповідно до принципів багатошарової структури (Controller → Service → Repository) із використанням Spring Boot.

Streaming subsystem відповідає за:

* видачу HLS playlist (`.m3u8`);
* видачу video segments (`.ts`);
* інтеграцію з movie metadata service;
* публікацію подій історії перегляду;
* абстракцію джерела зберігання streaming-файлів.

---

# Архітектурні шари

## 1. Controller Layer

### `VideoStreamingController`

Шлях:
`org.viora.viorastreamingcore.streaming.controller.VideoStreamingController`

REST API endpoint для streaming-запитів.

### Endpoint-и

#### Отримання HLS playlist

```http
GET /api/v1/streaming/movies/{id}/index.m3u8
```

Повертає playlist-файл типу:

```http
application/vnd.apple.mpegurl
```

#### Отримання video segment

```http
GET /api/v1/streaming/movies/{id}/segment_{segment}.ts
```

Повертає MPEG-TS segment типу:

```http
video/mp2t
```

Controller делегує всю бізнес-логіку через use-case interface `GetMovieUseCase`.

---

# 2. Service Layer

## `StreamingService`

Шлях:
`org.viora.viorastreamingcore.streaming.service.StreamingService`

Реалізація бізнес-логіки streaming subsystem.

### Основні обов’язки

#### Playback retrieval

* валідація movie id;
* отримання HLS playlist через repository.

#### Segment retrieval

* валідація параметрів;
* отримання video segment;
* публікація події історії перегляду.

---

## Event-driven integration

При кожному запиті segment викликається:

```java
publishEvent(id, segmentId)
```

### Логіка publishEvent

1. Отримання movie metadata через `MovieService`;
2. Отримання authenticated user id через `SecurityHelpers`;
3. Публікація `SaveHistoryCommand` через `ApplicationEventPublisher`.

Таким чином streaming subsystem інтегрується з history subsystem без прямої залежності.

---

# 3. Repository Layer

## `StreamingRepository`

Шлях:
`org.viora.viorastreamingcore.streaming.repository.StreamingRepository`

Абстракція джерела streaming-файлів.

### Методи

```java
Resource getMoviePlayback(String id)
Resource getMovieSegment(String id, Long segmentId)
```

Repository layer ізольовує service layer від конкретного storage implementation.

---

## `LocalMovieRepository`

Шлях:
`org.viora.viorastreamingcore.streaming.repository.LocalMovieRepository`

Поточна filesystem-based реалізація repository.

### Особливості

* використовується локальне файлове сховище;
* HLS assets читаються через `FileSystemResource`;
* segments зберігаються у структурі:

```text
{ROOT_SEGMENTS_FOLDER_PATH}/{movieId}/
```

### Формат файлів

```text
playlist.m3u8
segment_001.ts
segment_002.ts
...
```

---

# Segment Naming Strategy

Метод:

```java
private String getSegmentStringId(Long segment)
```

Виконує zero-padding segment identifier до 3 символів:

| Segment ID | Filename       |
| ---------- | -------------- |
| 1          | segment_001.ts |
| 15         | segment_015.ts |
| 120        | segment_120.ts |

Це забезпечує сумісність із HLS segment naming convention.

---

# Security Integration

Streaming subsystem інтегрований із security layer через:

```java
SecurityHelpers
```

Використовується для:

* визначення authenticated account;
* зв’язування streaming activity з user history.

---

# Event Flow

## Playback Request

```text
Client
  ↓
VideoStreamingController
  ↓
StreamingService
  ↓
StreamingRepository
  ↓
Filesystem
```

## Segment Request

```text
Client
  ↓
VideoStreamingController
  ↓
StreamingService
  ├─ publish SaveHistoryCommand
  └─ StreamingRepository
        ↓
     Filesystem
```

---

# Ключові модулі для RTM

## Streaming Module

```text
org.viora.viorastreamingcore.streaming
```

### Основні компоненти

| Компонент                  | Призначення                |
| -------------------------- | -------------------------- |
| `VideoStreamingController` | REST API для HLS streaming |
| `StreamingService`         | Бізнес-логіка streaming    |
| `StreamingRepository`      | Абстракція storage layer   |
| `LocalMovieRepository`     | Filesystem implementation  |

---

## Related Modules

### Content Module

```text
org.viora.viorastreamingcore.content
```

Використовується для отримання metadata фільмів.

---

### History Module

```text
org.viora.viorastreamingcore.history
```

Використовується для збереження історії перегляду через event publishing.

---

### Security Module

```text
org.viora.viorastreamingcore.configs.security
```

Використовується для отримання authenticated user context.
