\#Вибір архітектурного стилю для платформи Viora

\#Status - Accepted

\#Context

Платформа Viora є стримінговим веб-застосунком з інтегрованим AI-консультантом, що надає:
* Персоналізовані рекомендації фільмів
* Інтерактивний чат з AI
* Аналіз та обговорення контенту
* Управління списком перегляду (watchlist)

Ключові вимоги системи:
* Висока масштабованість (зростання до 10 000+ користувачів)
* Низька затримка для AI-взаємодії (chat, рекомендації)
* Гнучкість інтеграції (Google Gemini, Ollama)
* Безпечна робота з користувацькими даними
* Швидка розробка та time-to-market
* Підтримка SPA frontend (React)

\#Decision

Обрано Hybrid Architecture:

Основний стиль: Modular Monolith (модульний моноліт)

З елементами: Microservices-ready (еволюційна архітектура)

\#Rationale

Modular Monolith:
* Швидкий старт розробки
* Простота розгортання (один сервіс)
* Менше DevOps складності
* Низька складність на ранньому етапі
* Відсутність overhead мікросервісів (network latency, orchestration)
* Простота тестування
* Локальне тестування всієї системи
* Відповідність команді/ресурсам
* Ідеально для MVP та early-stage продукту

Microservices-ready:
* Майбутнє масштабування
* AI-сервіси можуть бути винесені окремо
* Recommendation engine може масштабуватись незалежно
* Ізоляція навантаження
* Chat AI ≠ Streaming ≠ User management
* Гнучкість технологій
* AI модулі можуть використовувати інші мови/стеки



\#Architecture Overview
Основні модулі (в межах моноліту):

* User Module
    Аутентифікація (Spring Security)
    Профілі користувачів
* Movie Module
    Каталог фільмів
    Фільтрація та пошук
* Recommendation Module
    Smart Recommendations
    Інтеграція з AI
* AI Module
    Chat з AI
    Інтеграція з Gemini / Ollama
* Watchlist Module
    Управління списком перегляду
* Streaming Module
    Відтворення контенту (або інтеграція з CDN)

\#Evolution Strategy
План переходу до мікросервісів:

Компонент		Коли виділяти		Причина

* AI Module		    Перший			Високе навантаження / latency
* Recommendation	Другий			ML scaling
* Streaming		    За потреби		CDN / media load
* User Service		Пізніше			Security isolation

\#Consequences
Позитивні:
* Швидкий запуск MVP
* Проста підтримка
* Менше інфраструктурних витрат
* Легка інтеграція AI

Негативні:
* Потенційні обмеження масштабування
* Ризик "монолітного спагеті" без дисципліни
* Складніший рефакторинг у майбутньому

\#Alternatives Considered

Повністю Microservices
Причини відмови:
* Занадто складно для MVP
* Високі DevOps витрати
* Overengineering

Serverless (FaaS)
Причини відмови:
* Не підходить для streaming
* Складність управління state
* Cold start latency

Layered Monolith
Причини відмови:
* Менша гнучкість масштабування
* Складніше виділяти сервіси

\#Decision Summary

Обрана архітектура:
Modular Monolith + Microservices-ready підхід

Це дозволяє:
* швидко запустити продукт
* забезпечити гнучкість для майбутнього росту
* ефективно інтегрувати AI-компоненти
