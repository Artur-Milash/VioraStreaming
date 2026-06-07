# Користувач

## UserRegisterRequestDTO

POST /api/v1/auth/register

{
  "email": "string",
  "password": "string",
  "fullName": "string"
}

Типи:
* email -> String (email format)
* password -> String (min 6 chars)
* fullName -> String

## UserResponseDTO

{
  "id": "uuid",
  "email": "string",
  "fullName": "string",
  "role": "string",
  "createdAt": "datetime"
}

Типи:
* id -> UUID
* email -> String
* fullName -> String
* role -> String (USER / ADMIN)
* createdAt -> ISO DateTime

# Фільм
## MovieCreateRequestDTO (Admin)

POST /api/v1/movies

{
  "title": "string",
  "description": "string",
  "genre": "string",
  "releaseYear": 2024,
  "durationMinutes": 120
}

Типи:
* title -> String
* description -> String
* genre -> String
* releaseYear -> Number (int)
* durationMinutes -> Number

## MovieResponseDTO

{
  "id": "uuid",
  "title": "string",
  "description": "string",
  "genre": "string",
  "releaseYear": 2024,
  "durationMinutes": 120,
  "rating": 8.5,
  "createdAt": "datetime"
}

Типи:
* id -> UUID
* title -> String
* description -> String
* genre -> String
* releaseYear -> Number
* durationMinutes -> Number
* rating -> Number (double)
* createdAt -> ISO DateTime

# Історія переглядів
## HistoryCreateRequestDTO

POST /api/v1/history

{
  "movieId": "uuid",
  "progressSeconds": 3600
}

Типи:
* movieId -> UUID
* progressSeconds -> Number

## HistoryResponseDTO

{
  "id": "uuid",
  "movie": {
    "id": "uuid",
    "title": "string"
  },
  "progressSeconds": 3600,
  "watchedAt": "datetime"
}

Типи:
* id -> UUID
* movie -> Object (вкладений DTO)
* progressSeconds -> Number
* watchedAt -> ISO DateTime

# AI Консультант
## AiMessageRequestDTO

POST /api/v1/ai/messages

{
  "message": "string"
}

Типи:
* message -> String

## AiMessageResponseDTO

{
  "response": "string",
  "suggestedMovies": [
    {
      "id": "uuid",
      "title": "string"
    }
  ]

}
Типи:
* response -> String
* suggestedMovies -> Array<Object>

## RecommendationResponseDTO

GET /api/v1/ai/recommendations

{
  "recommendations": [
    {
      "id": "uuid",
      "title": "string",
      "genre": "string",
      "rating": 8.7
    }
  ]
}

Типи:
* recommendations -> Array
* id -> UUID
* title -> String
* genre -> String
* rating -> Number