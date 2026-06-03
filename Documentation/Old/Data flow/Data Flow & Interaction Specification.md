# Схема взаємодії та потоків даних у системі Viora
# Purpose
Цей документ описує:
* як компоненти системи взаємодіють між собою
* як проходять потоки даних (Data Flow)
* ключові сценарії використання
* інтеграцію з зовнішніми сервісами (AI, email)

# System Overview
Система складається з:
* Frontend (React SPA)
* Backend (Spring Boot Modular Monolith)
* Database (PostgreSQL)
* AI Services (Gemini API, Ollama)
* Email Service (SendGrid)

# Key Interaction Scenarios
## User Registration & Authentication
Steps:
* Користувач вводить email/пароль
* Frontend відправляє запит /auth/register
* Backend:
    валідовує дані
    створює користувача
    зберігає у PostgreSQL
* Відправка email через SendGrid
* Повернення JWT токена

## Movie Search & Filtering
Steps:
* Користувач вводить параметри пошуку
* Запит /movies?genre=&year=
* Backend:
    формує query
    отримує список фільмів
* Повертає результат у frontend

## AI Movie Recommendation (Core Feature)
Steps:
* Користувач вводить запит:
    "Хочу щось легке і смішне"
* Backend:
    формує prompt
    передає в AIClient
* AI:
    генерує рекомендації
* Backend:
    нормалізує відповідь
    зберігає в історію (логи)
* Frontend відображає результат

## AI Chat Discussion
Steps:
* Користувач відкриває чат
* Відправляє повідомлення
* Backend:
    додає контекст (історія)
    викликає AI
* AI генерує відповідь
* Відповідь повертається користувачу

## Watchlist Management
Steps:
* Додавання/видалення фільму
* Запити:
    POST /watchlist
    DELETE /watchlist/{id}
* Дані зберігаються в БД


# Data Flow Types
## Synchronous Flow (REST API)

Використовується для:
* запитів користувача
* пошуку
* watchlist
* auth

Характеристики:
* HTTP/JSON
* швидка відповідь
* request-response модель

## Asynchronous Flow

Використовується для:
* email (SendGrid)
* потенційно AI jobs (у майбутньому)

Можливе розширення:
* message broker (RabbitMQ / Kafka)

## Data Storage Flow
Backend -> JPA/Hibernate -> PostgreSQL

Типи даних:
* Users
* Movies
* Watchlist
* Interaction history (AI)

## Error Handling Flow
Error -> Exception Handler -> Standard Response -> Frontend

Типи помилок:
* Validation errors
* AI timeout
* Database errors
* Unauthorized access

## Security Flow
User -> JWT Auth -> Security Filter -> Controller

Кроки:
* Login → отримання JWT
* Кожен запит містить token
* Backend валідовує token
* Доступ дозволено/відхилено

# Performance Considerations
Оптимізації:
* Кешування рекомендацій (Redis – майбутнє)
* Connection pooling (HikariCP)
* AI fallback (Gemini → Ollama)
* Pagination для списків

# Scalability Strategy
Потоки, які масштабуються першими:
* AI interactions
* Recommendation engine
* Streaming

# Future Improvements
* Event-driven architecture
* WebSocket для real-time chat
* AI streaming responses
* Analytics pipeline

# Summary
Система Viora використовує:  
* Основний тип взаємодії:
    Synchronous REST API
* Для AI:
    Hybrid AI Flow (cloud + local)
* Архітектурний підхід:
    Централізований backend з модульною логікою

Це забезпечує:
* швидку відповідь системи
* інтерактивний AI-досвід
* масштабованість у майбутньому