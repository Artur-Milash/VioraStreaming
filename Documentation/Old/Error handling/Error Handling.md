# Загальна ідея

Усі помилки API повертаються в єдиному форматі JSON, незалежно від типу помилки (валідація, авторизація, бізнес-логіка, серверні помилки).

Це дозволяє:
* фронтенду стабільно обробляти відповіді
* легко відображати помилки користувачу
* спрощує дебаг і логування

# Глобальна структура помилки

ErrorResponseDTO
{
  "timestamp": "2026-04-09T12:00:00Z",
  "errorCode": "STRING_CODE",
  "message": "Human-readable message",
  "details": [
    "Optional validation error 1",
    "Optional validation error 2"
  ]
}

# Опис полів
Поле	    Тип	                Обов'язковість	    Опис

timestamp	ISO 8601 (String)   YES	                Час виникнення помилки
errorCode	String	            YES	                Унікальний технічний код
message	    String	            YES	                Повідомлення (user/dev-friendly)
details	    Array<String>	    NO	                Деталі (валідація або уточнення)

# Приклади використання
## Валідаційна помилка (400 Bad Request)
{
  "timestamp": "2026-04-09T12:10:00Z",
  "errorCode": "VALIDATION_ERROR",
  "message": "Invalid request body",
  "details": [
    "email must contain @",
    "password must be at least 6 characters"
  ]
}

## Не знайдено ресурс (404 Not Found)
{
  "timestamp": "2026-04-09T12:11:00Z",
  "errorCode": "MOVIE_NOT_FOUND",
  "message": "Movie with given ID was not found"
}

## Помилка авторизації (401 Unauthorized)
{
  "timestamp": "2026-04-09T12:12:00Z",
  "errorCode": "UNAUTHORIZED",
  "message": "Authentication is required to access this resource"
}

## Заборонений доступ (403 Forbidden)
{
  "timestamp": "2026-04-09T12:13:00Z",
  "errorCode": "ACCESS_DENIED",
  "message": "You do not have permission to perform this action"
}

## Внутрішня помилка сервера (500 Internal Server Error)
{
  "timestamp": "2026-04-09T12:14:00Z",
  "errorCode": "INTERNAL_SERVER_ERROR",
  "message": "Unexpected error occurred"
}

# Стандартні errorCode
Загальні
* VALIDATION_ERROR
* INTERNAL_SERVER_ERROR
* BAD_REQUEST
Auth
* UNAUTHORIZED
* INVALID_CREDENTIALS
* USER_ALREADY_EXISTS
User
* USER_NOT_FOUND
Movie
* MOVIE_NOT_FOUND
AI
* AI_SERVICE_UNAVAILABLE
Access
* ACCESS_DENIED

# HTTP статуси та відповідність

HTTP Status	        errorCode
400	                VALIDATION_ERROR / BAD_REQUEST
401	                UNAUTHORIZED
403	                ACCESS_DENIED
404	                *_NOT_FOUND
409	                *_ALREADY_EXISTS
500	                INTERNAL_SERVER_ERROR

