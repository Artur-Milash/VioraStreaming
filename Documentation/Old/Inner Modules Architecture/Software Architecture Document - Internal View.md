# Внутрішня архітектура системи Viora

# Architectural Pattern

Обраний патерн: Layered Architecture (Багатошарова архітектура)

З елементами: Clean Architecture (Ports & Adapters / Hexagonal)

# Context

Система Viora є Modular Monolith, що включає:
* бізнес-логіку (рекомендації, AI взаємодія)
* роботу з базою даних (PostgreSQL)
* інтеграцію з зовнішніми AI сервісами (Gemini, Ollama)
* REST API для фронтенду (React SPA)

Основні виклики:
* складна бізнес-логіка (AI рекомендації)
* необхідність ізоляції залежностей
* масштабованість і підтримуваність коду
* можливість майбутнього переходу до мікросервісів

# Architecture Layers
## Presentation Layer (Controller Layer)

Технології:
* Spring Boot (REST Controllers)

Відповідальність:
* Обробка HTTP запитів
* Валідація input
* Виклик сервісів

Приклад:

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
}

## Application Layer (Service Layer)

Відповідальність:
* Виконання бізнес-логіки
* Виклик domain logic
* Координація між модулями

Приклад:

@Service
public class RecommendationService {
    public List<MovieDto> getRecommendations(User user) {}
}

## Domain Layer (Core Business Logic)

Включає:
* Entity
* Value Objects
* Business Rules

Властивості:
* Не залежить від framework
* Не залежить від бази даних

Приклад:

public class Movie {
    private String title;
    private Genre genre;
}

## Infrastructure Layer

Відповідальність:
* Робота з БД (Spring Data JPA)
* Інтеграція з AI API
* Email (SendGrid)

Приклад:

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {}

# Clean Architecture Elements
## Ports (Інтерфейси)

public interface AIClient {
    String getRecommendation(String prompt);
}

## Adapters (Реалізації)
@Component
public class GeminiAdapter implements AIClient {}

@Component
public class OllamaAdapter implements AIClient {}

# Dependency Rules
Основні правила:
* Controller → Service → Domain
* Domain не залежить ні від кого
* Infrastructure залежить від Domain
* Adapters реалізують Ports

# AI Integration Pattern
Використовується:
* Strategy Pattern

public class AIService {
    private AIClient client;
}

Можливість:
* перемикання між Gemini та Ollama
* fallback логіка

# Cross-Cutting Concerns

Security
* Spring Security (JWT)

Logging
* AOP / Logback

API Docs
* OpenAPI (Swagger)

Email
* SendGrid

# Advantages
* Чітке розділення відповідальностей
* Легка підтримка
* Гнучкість для змін
* Підготовка до мікросервісів
* Тестованість (unit testing)

# Disadvantages
* Більше boilerplate коду
* Вища складність структури
* Потрібна дисципліна команди

# Alternatives Considered
* MVC (простий)
    Недостатньо для складної логіки
* Pure Hexagonal
    Overengineering для MVP
* Microservices internal design
    Передчасна оптимізація

# Summary

Обраний підхід:
Layered Architecture + Clean Architecture (ядро)

Це дозволяє:
* масштабувати систему
* легко інтегрувати AI
* підтримувати код у довгостроковій перспективі
* підготувати систему до еволюції в мікросервіси