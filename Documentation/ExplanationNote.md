# Explanation Note for Viora Streaming Project

## 1. Architecture Description

## Overview

The platform is designed as a modern web application based on a layered architecture approach. The system consists of three main parts:

* Frontend (FE)
* Backend (BE)
* Database (DB)

The backend is implemented as a modular monolithic application using Spring Boot and follows the principles of separation of concerns, scalability, maintainability, and security.

The architecture is organized into independent business modules that communicate through service interfaces, repositories, events, and shared infrastructure components.

---

# Technology Stack and Rationale

## Backend

### Java

Java was selected as the primary programming language because it provides:

* high performance and reliability;
* strong ecosystem support;
* mature tooling;
* extensive enterprise adoption;
* long-term maintainability.

### Spring Boot

Spring Boot was chosen as the main backend framework because it offers:

* rapid application development;
* dependency injection and inversion of control;
* built-in REST API support;
* seamless integration with Spring Security;
* simplified configuration management;
* support for modular architecture.

### Spring Security

Spring Security provides:

* authentication and authorization;
* JWT integration;
* role-based access control;
* secure user session management;
* standardized security practices.

### Spring AI

Spring AI was selected for AI functionality because it provides:

* unified integration with LLM providers;
* prompt orchestration capabilities;
* conversational AI support;
* structured AI responses;
* seamless integration with the Spring ecosystem.

### JPA / Hibernate

JPA with Hibernate was chosen because it offers:

* object-relational mapping (ORM);
* database abstraction;
* automatic entity management;
* repository-based data access;
* support for complex relationships and dynamic queries.

### Thymeleaf

Thymeleaf is used for:

* HTML email rendering;
* dynamic email templates;
* verification and password recovery emails.

---

## Database

### Relational Database

The system uses a relational database managed through JPA entities and repositories.

Reasons for choosing a relational database include:

* strong consistency guarantees;
* support for complex relationships;
* transactional operations;
* reliable data integrity;
* efficient querying capabilities.

Core entities include:

* Account
* Movie
* History
* MovieDiscussion
* DiscussionMessage
* Verification entities

---

## Authentication

### JWT (JSON Web Tokens)

JWT was selected because it enables:

* stateless authentication;
* scalable API security;
* reduced server-side session management;
* easy frontend integration;
* secure access control for REST APIs.

---

# Backend Module Structure

The backend follows a modular architecture where each business domain is isolated into its own module.

## Core Modules

### Accounts Module

Responsible for:

* user registration;
* profile management;
* account deletion;
* email verification;
* password recovery.

Main components:

* AccountsController
* UserManagementService
* QueryAccountService
* AccountRepository
* AccountModel

---

### Authentication Module

Responsible for:

* user login;
* credential verification;
* JWT token generation;
* authentication workflows.

Main components:

* AuthController
* AuthService
* JwtTokenService

---

### Content Module

Responsible for:

* movie catalog management;
* movie retrieval;
* search and filtering;
* metadata management.

Main components:

* MovieController
* MovieServiceImpl
* MovieRepository
* MovieSpecification

---

### AI Module

Responsible for:

* mood-based movie recommendations;
* AI-powered movie discussions;
* LLM integration;
* recommendation personalization.

Main components:

* MoodMovieController
* MovieDiscussionController
* MoodMovieService
* MovieDiscussionService

---

### History Module

Responsible for:

* watch history tracking;
* playback progress persistence;
* recommendation context generation.

Main components:

* HistoryController
* GetHistoryService
* HistoryService
* HistoryRepository

---

### Streaming Module

Responsible for:

* HLS video streaming;
* playlist delivery;
* segment delivery;
* playback event publishing.

Main components:

* VideoStreamingController
* StreamingService
* StreamingRepository

---

### Mail Module

Responsible for:

* email delivery;
* template rendering;
* verification emails;
* password recovery emails.

Main components:

* SendGridMailService
* EmailMessage
* VerifyEmailMessage

---

### Infrastructure & Configurations Module

Provides shared infrastructure services:

* security configuration;
* JWT infrastructure;
* exception handling;
* serialization configuration;
* template engine configuration;
* utility services.

Main components:

* SecurityConfigs
* JwtAuthFilter
* JwtTokenService
* DefaultRestExceptionHandler
* SecurityHelpers

---

# Layered Architecture

The backend follows a layered architecture pattern.

## Controller Layer

Responsibilities:

* exposing REST endpoints;
* request validation;
* request/response handling;
* delegating business logic to services.

Examples:

* AccountsController
* AuthController
* MovieController
* HistoryController

---

## Service Layer

Responsibilities:

* business logic implementation;
* orchestration of workflows;
* integration between modules;
* transaction management.

Examples:

* UserManagementService
* AuthService
* MovieServiceImpl
* MoodMovieService

---

## Repository Layer

Responsibilities:

* database communication;
* CRUD operations;
* custom queries;
* entity persistence.

Examples:

* AccountRepository
* MovieRepository
* HistoryRepository

---

## DTO Layer

Responsibilities:

* API contracts;
* request validation;
* response serialization;
* separation of domain models from API models.

Examples:

* AccountDto
* MovieDto
* LoginUserRequest
* MoodMovieSuggestion

---

## Persistence Layer

Responsibilities:

* entity mapping;
* database schema representation;
* relationship management.

Examples:

* AccountModel
* Movie
* History
* MovieDiscussion

---

# Communication Between Layers

## Backend Internal Flow

A typical request follows the sequence:

```text
Client
   |
Controller
   |
Service
   |
Repository
   |
Database
```

Response flow:

```text
Database
   |
Repository
   |
Service
   |
DTO Mapping
   |
Controller
   |
Client
```

---

# Frontend, Backend and Database Communication

## Frontend -> Backend

The frontend communicates with the backend through REST APIs using HTTP/HTTPS.

Typical interactions include:

* user authentication;
* movie search;
* movie retrieval;
* AI recommendations;
* streaming requests;
* watch history access.

Example:

```text
Frontend
    | HTTP Request
/api/v1/auth
    |
Backend
```

All requests and responses use JSON, except streaming endpoints which return HLS resources.

---

## Backend -> Database

The backend communicates with the database through:

* Spring Data JPA;
* Hibernate ORM;
* Repository interfaces.

Flow:

```text
Service
    |
Repository
    |
Hibernate/JPA
    |
Database
```

The backend is the only component allowed to access the database directly.

---

## Frontend -> Backend -> Database Flow

Example: User Login

```text
Frontend
    |
POST /api/v1/auth
    |
AuthController
    |
AuthService
    |
AccountRepository
    |
Database

Database
    |
AuthService
    |
JWT Token
    |
Frontend
```

---

## AI Recommendation Flow

```text
Frontend
    |
MoodMovieController
    |
MoodMovieService
    |
MovieService
    |
Database

MoodMovieService
    |
Spring AI / LLM
    |
Recommendation Result
    |
Frontend
```

---

## Streaming Flow

```text
Frontend Video Player
    |
Streaming API
    |
StreamingService
    |
StreamingRepository
    |
File Storage

StreamingService
    |
History Event
    |
History Module
    |
Database
```

---

# Security Architecture

Authentication and authorization are implemented through Spring Security and JWT.

Security flow:

```text
Login
   |
JWT Generation
   |
Frontend Stores Token
   |
Authenticated Requests
   |
JwtAuthFilter
   |
SecurityContext
   |
Protected Resources
```

Key security components:

* Spring Security
* JwtAuthFilter
* JwtTokenService
* SecurityHelpers
* UserDetailsService

---

# Architectural Benefits

The selected architecture provides:

* clear separation of responsibilities;
* high maintainability;
* modular business domains;
* scalable API design;
* secure authentication and authorization;
* reusable infrastructure services;
* AI integration capabilities;
* event-driven communication where appropriate;
* simplified testing and future extensibility.

The architecture is designed to support future growth while maintaining code quality, security, and performance.

# Frontend Architecture

## Overview

The frontend application is implemented using **React** and **TypeScript** and follows a component-based architecture.

The application communicates with the backend through REST APIs and uses JWT authentication for secure access to protected resources.

### Main Frontend Technologies

| Technology              | Purpose                           |
| ----------------------- | --------------------------------- |
| React                   | User Interface development        |
| TypeScript              | Static typing and maintainability |
| React Router DOM        | Client-side routing               |
| Redux Toolkit           | Global state management           |
| Fetch/Axios API Clients | Backend communication             |
| Local Storage           | JWT token persistence             |

### Benefits of the Selected Frontend Stack

The selected frontend technologies provide:

* strong type safety;
* reusable UI components;
* scalable application structure;
* centralized state management;
* predictable routing behavior;
* maintainable codebase;
* seamless integration with REST APIs.

---

# Frontend Project Structure

```text
src/
--- api/
--- assets/
--- components/
--- constants/
--- contexts/
--- hooks/
--- models/
--- pages/
--- routes/
--- store/
--- utils/
--- App.tsx
--- main.tsx
```

## Folder Responsibilities

| Folder     | Responsibility                      |
| ---------- | ----------------------------------- |
| api        | API clients and request handlers    |
| assets     | Images, icons, and static resources |
| components | Reusable UI components              |
| constants  | Application constants               |
| contexts   | React Context providers             |
| hooks      | Custom React hooks                  |
| models     | TypeScript interfaces and models    |
| pages      | Route-level pages                   |
| routes     | Route definitions and route guards  |
| store      | Redux Toolkit state management      |
| utils      | Utility functions                   |

---

# Frontend Routing Architecture

Routing is implemented using React Router.

## Main Routes

```typescript
export const API_PAGE = {
  Auth: "/auth",
  Home: "/home",
  Movies: "/movies",
  History: "/history",
  Assistant: "/assistant",
  Settings: "/settings"
}
```

### Authentication Routes

```typescript
export const PAGE_ROUTES = {
  Register: "register",
  ForgotPassword: "forgot-password",
  DropPassword: "drop-password"
}
```

---

## Route Guards

### ProtectedRoute

Provides access control for authenticated users.

Responsibilities:

* validate JWT presence;
* protect application pages;
* redirect unauthenticated users to login.

Flow:

```text
User Request
      |
ProtectedRoute
      |
JWT exists?
 │         │
Yes        No
 │         │
Render   Redirect
Page     /auth
```

---

### AnonRoute

Prevents authenticated users from accessing authentication pages.

Flow:

```text
User Request
      |
AnonRoute
      |
JWT exists?  
 │         │
Yes        No
 │         │
Redirect   Allow
/home      Access
```

---

# Redux Store Architecture

State management is implemented using Redux Toolkit.

## Global Store Structure

```text
store/
--- auth.ts
--- filterSlice.ts
--- modals.ts
--- store.ts
```

---

## Authentication Slice

### Responsibilities

* JWT token storage;
* login persistence;
* logout handling;
* synchronization with localStorage.

### State

```typescript
{
  token: string | null
}
```

---

## Filters Slice

Used for movie catalog filtering.

### Stored Filters

* genres;
* rating;
* release year;
* duration;
* title search.

This state directly integrates with the Content Module search API.

---

## Modals Slice

Implements a stack-based modal architecture.

Responsibilities:

* modal creation;
* modal closing;
* multiple modal support.

Flow:

```text
openModal()
     |
Push to Stack

closeModal()
     |
Pop from Stack
```

---

## Global State Shape

```typescript
RootState = {
  auth: {
    token: string | null
  },
  modal: {
    stack: Modal[]
  },
  filters: {
    genres: number[]
    rating: number
    releaseYear: number[]
    duration: string
    title: string
  }
}
```

---

# Frontend Error Handling

Centralized error configuration is used.

Supported error scenarios:

| Status | Description           |
| ------ | --------------------- |
| 401    | Unauthorized          |
| 403    | Forbidden             |
| 404    | Page Not Found        |
| 500    | Internal Server Error |

Benefits:

* consistent UX;
* centralized maintenance;
* reusable error pages.

---

# Verification Module

## Purpose

The Verification Module is responsible for secure user verification and account activation workflows.

Main responsibilities:

* email verification;
* password reset verification;
* JWT-based verification token generation;
* verification email delivery;
* callback execution after successful verification.

The module follows a layered architecture combined with the Strategy Pattern.

```text
Controller
    |
Verification Service
    |
Verification Strategy
    |
Token Issuer / Mail Service / Domain Services
```

---

## Main Components

### Controller Layer

**AccountsVerificationController**

Endpoints:

| Method | Endpoint                                      |
| ------ | --------------------------------------------- |
| GET    | `/api/v1/verification/accounts/register`      |
| GET    | `/api/v1/verification/accounts/drop-password` |

Responsibilities:

* verification entry point;
* token validation initiation;
* frontend callback redirection.

---

### Service Layer

**StrategyVerificationService**

Responsibilities:

* verification orchestration;
* strategy selection;
* verification execution.

---

### Strategy Layer

Implemented using the Strategy Pattern.

#### EmailVerificationStrategy

Responsible for:

* email confirmation;
* account activation;
* verification token validation.

#### DropPasswordVerificationStrategy

Responsible for:

* password reset verification;
* secure callback execution;
* temporary secure cookie handling.

---

### Token Management

**JwtVerificationTokenIssuer**

Responsibilities:

* JWT token generation;
* JWT validation;
* verification claim validation.

Verification tokens contain:

```text
issuer
subject (email)
expiration
verification claims
```

---

## Verification Types

```java
VERIFY_EMAIL
VERIFY_DROP_PASSWORD
```

---

## Verification Flow

### Email Verification

```text
User Clicks Link
        |
AccountsVerificationController
        |
StrategyVerificationService
        |
EmailVerificationStrategy
        |
JWT Validation
        |
Account Activation
```

---

### Password Reset Verification

```text
User Clicks Link
        |
AccountsVerificationController
        |
DropPasswordVerificationStrategy
        |
JWT Validation
        |
Secure Cookie Creation
        |
Frontend Reset Flow
```

---

# Updated Module Structure

The backend consists of the following business modules:

| Module                   | Responsibility                                   |
| ------------------------ | ------------------------------------------------ |
| Accounts                 | User management and profiles                     |
| Authentication           | Login and JWT authentication                     |
| Verification             | Email verification and password reset validation |
| Content                  | Movie catalog and search                         |
| AI                       | Recommendations and discussions                  |
| History                  | Watch history and playback tracking              |
| Streaming                | HLS video streaming                              |
| Mail                     | Email delivery and templates                     |
| Configs & Infrastructure | Security and shared infrastructure               |

---

# Updated Frontend–Backend–Database Interaction

## Authentication Flow

```text
Frontend
    |
POST /api/v1/auth
    |
AuthController
    |
AuthService
    |
AccountRepository
    |
Database

Database
    |
AuthService
    |
JWT Token
    |
Redux Store
    |
Local Storage
```

---

## Registration & Verification Flow

```text
Frontend
    |
POST /accounts/register
    |
AccountsController
    |
UserManagementService
    |
VerificationService
    |
Mail Module
    |
Verification Email

User Clicks Link
    |
AccountsVerificationController
    |
Verification Strategy
    |
Account Activation
    |
Frontend Redirect
```

---

## Password Recovery Flow

```text
Frontend
    |
Request Password Reset
    |
DropAccountsPasswordService
    |
VerificationService
    |
Mail Module

User Clicks Link
    |
Verification Controller
    |
JWT Validation
    |
Secure Cookie
    |
Frontend Reset Page
    |
Password Update
```

---

## Complete System Architecture

```text
Frontend (React + TypeScript)
            |
       REST API
            |
Backend (Spring Boot)
            |
       JPA/Hibernate
            |
      Relational DB

Additional Integrations:
    |
Spring Security
JWT Infrastructure
Spring AI
SMTP/Email Services
Local HLS Storage
```

# 2. Quality Metrics


# 3. Conclusions

## Implemented Functionality (MVP Results)

The development of **Viora** successfully delivered a functional Minimum Viable Product (MVP) that demonstrates the core concept of an AI-powered movie streaming platform. The implemented solution combines traditional streaming service capabilities with intelligent recommendation and discussion features powered by artificial intelligence.

### User Management and Security

A complete authentication and account management system was implemented using JWT-based security mechanisms. Users can register, authenticate, verify their email addresses, and recover access to their accounts through a secure password reset workflow.

The verification subsystem was designed using the **Strategy Pattern**, allowing multiple verification flows to coexist while maintaining extensibility. The implemented verification module supports:

* Email verification during account registration
* Password reset verification
* JWT-based verification token generation and validation
* Secure email delivery through the mail subsystem
* Callback-driven verification workflows
* Secure HTTP-only cookie handling for password recovery scenarios

The backend architecture follows a layered approach:

```text
Controller -> Service -> Strategy -> Infrastructure
```

This design improves maintainability, testability, and future extensibility of the platform.

### Movie Catalog and Content Discovery

The platform provides a movie catalog that allows users to browse available content and access detailed movie information. Search and filtering capabilities were implemented to help users efficiently discover content based on their preferences.

Implemented filtering functionality includes:

* Movie title search
* Genre filtering
* Rating filtering
* Release year filtering
* Duration filtering

Frontend state management for filtering is centralized through Redux Toolkit, ensuring a consistent user experience across the application.

### Streaming Platform Functionality

The MVP includes the fundamental components required for an online streaming platform:

* Movie browsing interface
* Movie playback functionality
* Viewing history pages
* User settings management
* Protected access to authenticated content

The application routing system includes authentication guards that restrict access to protected resources while preventing authenticated users from accessing authorization pages unnecessarily.

### AI Assistant Integration

One of the primary objectives of the project was the integration of artificial intelligence into the streaming experience. This objective was successfully achieved through the implementation of the **AI Movie Assistant**.

The assistant leverages Google Gemini and Ollama models to provide:

* Personalized movie recommendations
* Interactive movie selection assistance
* Conversational support for discussing movies and series
* Intelligent content discovery

This functionality differentiates Viora from traditional streaming platforms by enabling a more interactive and personalized user experience.

### Frontend Architecture

The frontend application was implemented using React and TypeScript with a scalable project structure.

Key frontend achievements include:

* Component-based architecture
* Centralized routing management
* Protected and anonymous route guards
* Redux Toolkit state management
* Error handling and fallback pages
* Reusable UI components
* Type-safe application architecture

The Redux store was organized into dedicated slices for:

* Authentication management
* Movie filtering
* Modal management

This architecture ensures scalability and maintainability as the application grows.

### Backend Architecture

The backend was developed using Spring Boot and follows modern enterprise architectural principles.

Implemented backend capabilities include:

* REST API architecture
* JWT authentication and authorization
* Verification subsystem
* Strategy-based business logic execution
* Email notification workflows
* PostgreSQL integration for structured data
* MongoDB integration for flexible document storage
* Modular package organization

The verification module demonstrates the project's emphasis on clean architecture and extensibility through the use of design patterns and dependency injection.

### Overall MVP Outcome

The completed MVP successfully validates the project's core vision of combining movie streaming functionality with AI-powered content discovery and discussion capabilities.

Users can:

* Create and verify accounts
* Authenticate securely
* Browse movie content
* Search and filter movies
* Watch available content
* Interact with the AI assistant
* Recover account access when necessary

The resulting platform demonstrates the technical feasibility of the Viora concept and establishes a solid foundation for future development.

---

# Backlog and Future Development

Although the MVP delivers the project's core functionality, several planned features were intentionally postponed for future iterations to maintain focus on the most critical business requirements.

## Personal Watchlist

The product vision includes a personal watchlist feature that allows users to save movies and series for future viewing.

Planned capabilities:

* Add movies to watchlist
* Remove movies from watchlist
* Watchlist categorization
* Synchronization across devices

## Advanced Smart Recommendations

While AI-assisted recommendations are available through the chat assistant, a fully automated recommendation engine remains part of the backlog.

Future improvements include:

* Recommendation generation based on viewing history
* Behavioral analysis
* Preference learning
* Personalized home page suggestions
* Recommendation ranking algorithms

## Enhanced AI Features

Future releases are expected to expand AI capabilities significantly.

Planned enhancements include:

* Persistent conversational memory
* User preference profiling
* Context-aware recommendations
* Advanced movie analysis
* Multi-turn discussion history
* Improved personalization mechanisms

## User Reviews and Ratings

Community-driven content evaluation was excluded from the MVP scope.

Planned functionality:

* Movie ratings
* User reviews
* Review moderation
* Community recommendations

## Subscription and Monetization

Monetization features were intentionally excluded from the initial release.

Future implementation may include:

* Premium subscription plans
* AI premium features
* Subscription management
* Billing integration
* Payment processing

## Content Management Expansion

Future versions of the platform may include additional content management capabilities:

* Automated movie metadata synchronization
* Integration with external movie databases
* Expanded content catalogs
* Content administration tools

## Analytics and Personalization

Several analytical features remain in the backlog:

* User engagement analytics
* Viewing statistics
* Recommendation effectiveness tracking
* Personalized content insights

## Streaming Optimization

The MVP provides basic streaming functionality, while future releases will focus on performance improvements.

Planned enhancements:

* Adaptive bitrate streaming
* Improved media delivery optimization
* Enhanced playback performance
* Multi-device streaming optimization

## Security Improvements

Additional security features planned for future iterations include:

* Multi-factor authentication (MFA)
* Advanced account protection
* Session management improvements
* Security monitoring and auditing

## Mobile Applications

The current implementation focuses on a web-based platform.

Future development may include:

* Native Android application
* Native iOS application
* Cross-platform mobile support
* Mobile-specific user experience enhancements

---

# Final Assessment

The Viora MVP successfully achieves the primary project goals defined in the Product Vision and Business Requirements documentation. The platform delivers a working streaming environment enhanced by AI-powered assistance, secure user management, and a scalable software architecture.

The implemented solution demonstrates that combining movie streaming with artificial intelligence can create a more personalized, interactive, and engaging user experience. Furthermore, the modular frontend and backend architectures provide a strong foundation for future expansion, enabling the project to evolve toward its long-term vision of becoming a fully featured AI-driven entertainment platform.

---

# Additional Technical Contributions by Role

## Database Engineer

### Final Database Design (ERD)

The database architecture of the Viora platform was designed to support user management, authentication, movie catalog operations, streaming-related functionality, AI-assisted interactions, and future scalability.

The system utilizes a hybrid persistence approach:

* **PostgreSQL** for structured relational data
* **MongoDB** for flexible AI-related and conversational data storage

### Entity Relationship Diagram (ERD)

```text
Account
│
|-- WatchHistory
│
|-- RefreshToken
│
|-- VerificationToken
│
|-- Recommendation

Movie
│
|-- WatchHistory
│
|-- Recommendation
│
|-- MovieGenre -- Genre
```

---

## Core Relational Tables

### Account

Stores registered user information and authentication-related data.

| Column     | Type      | Description               |
| ---------- | --------- | ------------------------- |
| id         | UUID      | Primary key               |
| email      | VARCHAR   | Unique user email         |
| password   | VARCHAR   | Encrypted password        |
| enabled    | BOOLEAN   | Account activation status |
| created_at | TIMESTAMP | Registration date         |
| updated_at | TIMESTAMP | Last update timestamp     |

#### Relationships

* One Account can have multiple WatchHistory records.
* One Account can have multiple RefreshTokens.
* One Account can receive multiple Recommendations.
* One Account participates in verification workflows.

---

### RefreshToken

Stores refresh tokens used for JWT authentication.

| Column     | Type      | Description            |
| ---------- | --------- | ---------------------- |
| id         | UUID      | Primary key            |
| token      | VARCHAR   | Refresh token value    |
| expires_at | TIMESTAMP | Expiration date        |
| account_id | UUID      | Foreign key to Account |

#### Relationships

* Many RefreshTokens belong to one Account.

---

### VerificationToken

Stores verification metadata associated with account actions.

| Column            | Type      | Description                          |
| ----------------- | --------- | ------------------------------------ |
| id                | UUID      | Primary key                          |
| account_id        | UUID      | Foreign key to Account               |
| verification_type | VARCHAR   | Email verification or password reset |
| created_at        | TIMESTAMP | Creation timestamp                   |

#### Relationships

* Many verification records may belong to one Account.

---

### Movie

Stores movie and series metadata displayed in the platform catalog.

| Column       | Type    | Description        |
| ------------ | ------- | ------------------ |
| id           | UUID    | Primary key        |
| title        | VARCHAR | Movie title        |
| description  | TEXT    | Movie synopsis     |
| release_year | INTEGER | Release year       |
| duration     | INTEGER | Runtime in minutes |
| rating       | DECIMAL | Movie rating       |
| poster_url   | VARCHAR | Poster image       |
| video_url    | VARCHAR | Streaming source   |

#### Relationships

* One Movie may belong to multiple Genres.
* One Movie may appear in multiple WatchHistory records.
* One Movie may be referenced in Recommendations.

---

### Genre

Stores movie genre information.

| Column | Type    | Description |
| ------ | ------- | ----------- |
| id     | INTEGER | Primary key |
| name   | VARCHAR | Genre name  |

#### Relationships

* Many-to-many relationship with Movie.

---

### MovieGenre

Junction table implementing the many-to-many relationship between movies and genres.

| Column   | Type    |
| -------- | ------- |
| movie_id | UUID    |
| genre_id | INTEGER |

---

### WatchHistory

Stores information about content viewed by users.

| Column     | Type      | Description       |
| ---------- | --------- | ----------------- |
| id         | UUID      | Primary key       |
| account_id | UUID      | User identifier   |
| movie_id   | UUID      | Movie identifier  |
| watched_at | TIMESTAMP | Viewing timestamp |

#### Relationships

* Many WatchHistory records belong to one Account.
* Many WatchHistory records reference one Movie.

---

### Recommendation

Stores personalized recommendations generated for users.

| Column     | Type      | Description              |
| ---------- | --------- | ------------------------ |
| id         | UUID      | Primary key              |
| account_id | UUID      | User identifier          |
| movie_id   | UUID      | Recommended movie        |
| created_at | TIMESTAMP | Recommendation timestamp |

#### Relationships

* Many Recommendations belong to one Account.
* Many Recommendations reference one Movie.

---

## MongoDB Collections

### AI Conversations

Stores chat sessions between users and the AI Movie Assistant.

```json
{
  "_id": "ObjectId",
  "accountId": "UUID",
  "messages": [
    {
      "role": "user",
      "content": "Recommend a sci-fi movie"
    },
    {
      "role": "assistant",
      "content": "You may enjoy Interstellar..."
    }
  ],
  "createdAt": "timestamp"
}
```

### Purpose

* Preserve AI conversation history
* Support contextual recommendations
* Enable future personalization features
* Improve AI-assisted movie discussions

---

## Database Relationships Summary

| Source Entity | Relationship | Target Entity     |
| ------------- | ------------ | ----------------- |
| Account       | 1:N          | RefreshToken      |
| Account       | 1:N          | WatchHistory      |
| Account       | 1:N          | Recommendation    |
| Account       | 1:N          | VerificationToken |
| Movie         | N:M          | Genre             |
| Movie         | 1:N          | WatchHistory      |
| Movie         | 1:N          | Recommendation    |
| Genre         | N:M          | Movie             |

---

## Database Design Rationale

The database architecture was designed according to the following principles:

* Separation of authentication and business data
* Efficient querying of movie catalogs and filtering operations
* Scalability for future recommendation systems
* Support for AI-generated content and conversations
* Clear relational integrity through foreign keys
* Extensibility for future features such as watchlists, subscriptions, ratings, and reviews

The combination of PostgreSQL and MongoDB enables Viora to leverage the strengths of both relational and document-oriented databases while maintaining a clean and scalable architecture.

---

## Backend Developer

### Complex Algorithms and Security Mechanisms

The backend of the Viora platform was designed with a strong emphasis on security, scalability, and maintainability. Several advanced mechanisms were implemented to protect user data, secure API communication, and support reliable business processes.

---

# JWT-Based Authentication and Authorization

## Overview

The platform uses **JSON Web Tokens (JWT)** as the primary authentication mechanism. JWT enables stateless authentication, reducing server-side session management overhead while providing secure user identification across requests.

The authentication workflow consists of:

1. User authentication
2. Access token generation
3. Refresh token management
4. Token validation on protected endpoints

---

## Authentication Flow

```text
User Login
    |
Credentials Validation
    |
JWT Access Token Generated
    |
Refresh Token Generated
    |
Client Stores Token
    |
Protected API Requests
    |
JWT Validation
```

---

## Access Token Structure

The access token contains authenticated user information and authorization claims.

Example payload:

```json
{
  "sub": "user@example.com",
  "iat": 1710000000,
  "exp": 1710003600,
  "roles": ["USER"]
}
```

### Key Fields

| Claim | Description                |
| ----- | -------------------------- |
| sub   | User identifier (email)    |
| iat   | Token issue timestamp      |
| exp   | Token expiration timestamp |
| roles | User roles and permissions |

---

## Refresh Token Mechanism

To improve security and user experience, refresh tokens are used alongside access tokens.

### Benefits

* Short-lived access tokens reduce attack exposure.
* Users remain authenticated without repeated logins.
* Compromised access tokens become invalid quickly.

### Flow

```text
Access Token Expires
        |
Client Sends Refresh Token
        |
Server Validates Refresh Token
        |
New Access Token Issued
```

Refresh tokens are stored in the database and linked to user accounts, allowing revocation when necessary.

---

# Verification Token Algorithm

## Purpose

A dedicated JWT verification mechanism was implemented for account-related workflows:

* Email verification
* Password reset verification

Unlike authentication tokens, verification tokens contain specific business claims used to validate intended actions.

---

## Token Generation

The verification subsystem uses a dedicated component:

```text
JwtVerificationTokenIssuer
```

Verification tokens contain:

```text
Issuer
Subject (User Email)
Expiration Timestamp
Verification Claims
```

Example structure:

```json
{
  "sub": "user@example.com",
  "verify_email": true,
  "exp": 1710003600
}
```

---

## Verification Algorithm

### Email Verification

```text
Receive Token
      |
Validate JWT Signature
      |
Check Expiration
      |
Verify verify_email Claim
      |
Enable Account
```

The account becomes active only after successful validation.

---

### Password Reset Verification

```text
Receive Token
      |
Validate JWT Signature
      |
Check Expiration
      |
Verify drop_password Claim
      |
Return Email Through Callback
```

The token is then stored in a secure HTTP-only cookie for further password reset operations.

---

# Strategy-Based Verification Algorithm

A major architectural contribution was the implementation of a verification system using the **Strategy Pattern**.

## Motivation

Different verification workflows require different business logic:

* Email activation
* Password reset
* Future verification types

Instead of using conditional statements throughout the codebase, the system dynamically selects the correct strategy.

---

## Strategy Selection Algorithm

```text
Verification Request
        |
Verification Type
        |
StrategyVerificationService
        |
Find Matching Strategy
        |
Execute Verification Logic
```

### Runtime Mapping

```text
VerificationType
        |
List<VerificationStrategy>
```

This design enables:

* Open/Closed Principle compliance
* Easy extension of verification scenarios
* Reduced coupling between services

---

# Input Validation Mechanisms

## Request Validation

Incoming API requests are validated before business logic execution.

Validation includes:

* Required field verification
* Email format validation
* Password complexity requirements
* Null checks
* Length restrictions
* Data type validation

Example constraints:

| Field        | Validation                  |
| ------------ | --------------------------- |
| Email        | Valid email format          |
| Password     | Minimum length              |
| Token        | Non-empty value             |
| Search Query | Maximum length restrictions |

---

## Business Validation

In addition to request-level validation, domain-specific validation rules are applied.

Examples:

* Prevent duplicate user registration
* Verify account existence
* Check account activation status
* Validate token ownership
* Prevent unauthorized operations

---

# Password Security

## Password Hashing

User passwords are never stored in plain text.

The backend applies secure one-way hashing before persistence.

```text
User Password
      |
Password Encoder
      |
Hashed Password
      |
Database Storage
```

### Security Benefits

* Protection against database leaks
* Resistance to credential theft
* Compliance with modern security practices

---

# Secure Cookie Protection

The password reset flow uses secure browser cookies.

Cookie configuration:

```text
HttpOnly = true
Secure = true
Path = "/"
Expiration = 15 minutes
```

### Security Advantages

* Prevents JavaScript access
* Reduces XSS attack risks
* Limits token exposure
* Ensures encrypted transmission over HTTPS

---

# API Protection

Protected endpoints require successful JWT validation before execution.

Request processing flow:

```text
Incoming Request
        |
JWT Filter
        |
Token Validation
        |
Authentication Context Creation
        |
Controller Access
```

Unauthorized requests are rejected before reaching business logic.

---

# Exception Handling and Error Security

The backend implements centralized exception handling to ensure consistent and secure error responses.

### Objectives

* Prevent information leakage
* Standardize API responses
* Simplify debugging
* Improve client-side error handling

Example protected responses:

```json
{
  "message": "Unauthorized access",
  "status": 401
}
```

Sensitive internal details such as stack traces, SQL errors, or infrastructure information are never exposed to clients.

---

# Frontend Developer

## Interface Component Logic and State Management Structure

### Overview

The frontend of **Viora** is implemented using **React**, **TypeScript**, and **Material UI (MUI)**. The architecture follows a modular, component-based design that separates presentation logic, business logic, API communication, and state management into dedicated layers.

The application is responsible for:

* User authentication and authorization flows
* Movie catalog browsing and filtering
* Video streaming interface
* AI-powered movie recommendation interactions
* Movie discussion chats
* Watch history management
* User account settings

---

# Application Architecture

The frontend application is initialized through the root `App` component.

```text
App
  -- ThemeProvider
  --  Redux Provider
  --  React Query Provider
  --  Router
  --  ModalContainer
  -- Toast Notifications
```

### Root Component Responsibilities

#### ThemeProvider

Provides a centralized dark theme configuration using Material UI.

Features:

* Custom color palette
* Global styling consistency
* Typography and component styling inheritance

#### Redux Provider

Provides global application state management.

Used for:

* Authentication state
* Modal management
* Movie filter state

#### React Query Provider

Handles:

* Server state management
* Request caching
* Automatic request lifecycle handling
* Error handling integration

#### ModalContainer

Responsible for rendering active modal windows from the Redux modal stack.

#### Toast

Displays transient notifications and user feedback messages.

---

# Routing Architecture

Routing is implemented using `react-router-dom`.

The application uses a nested routing structure combined with route guards.

## Route Categories

### Public Routes

Accessible only to unauthenticated users.

Examples:

```text
/auth/register
/auth/forgot-password
/auth/drop-password
```

Protected by:

```text
AnonRoute
```

Behavior:

```text
User authenticated
    |
Redirect to /home

User not authenticated
    |
Allow access
```

---

### Protected Routes

Accessible only to authenticated users.

Examples:

```text
/Movies
```

Protected by:

```text
ProtectedRoute
```

Behavior:

```text
JWT token exists
    |
Render page

JWT token missing
    |
Redirect to /auth
```

---

### Application Layout Routes

Authenticated users access the application through a shared layout.

```text
AppLayoutWithSideNav
  -- Home
  --  Assistant
  --  History
  --  Settings
  -- Error Pages
```

This layout provides:

* Persistent navigation sidebar
* Shared application shell
* Consistent user experience across pages

---

# State Management Architecture

The application uses a hybrid state management approach:

```text
Redux Toolkit
        +
React Query
```

---

## Redux Toolkit

Redux is used for client-side global state.

Store structure:

```text
RootState
 -- auth
 --  modal
 -- filters
```

---

# Authentication State

## Purpose

Stores user authentication information.

State:

```ts
{
  token: string | null
}
```

### Responsibilities

* Persist JWT token
* Maintain login session
* Control route access
* Handle logout operations

### Token Persistence

Authentication data is stored in:

```text
localStorage
```

Stored values:

```text
JWT_TOKEN
JWT_TOKEN_EXPIRY
```

Token expiration is validated on every retrieval.

Flow:

```text
Login
 |
Save JWT
 |
Save Expiration Timestamp
 |
Protected Routes Enabled
```

---

# Filters State

## Purpose

Stores movie catalog filtering preferences.

State:

```ts
{
  genres: number[]
  rating: number
  releaseYear: number[]
  duration: string
  title: string
}
```

### Supported Filters

* Genre
* Rating
* Release year range
* Duration
* Search title

### Benefits

* Centralized filter management
* Consistent filtering across components
* Easy reset functionality

---

# Modal State

## Purpose

Controls application dialogs and modal windows.

State:

```ts
{
  stack: Modal[]
}
```

Each modal contains:

```ts
{
  id: string
  type: ModalTypes
  data: unknown
}
```

### Stack-Based Modal System

The implementation supports multiple modal windows.

Operations:

```text
openModal()
    |
Push modal onto stack

closeModal()
    |
Pop modal from stack
```

Advantages:

* Supports nested dialogs
* Predictable modal lifecycle
* Centralized modal rendering

---

# Server State Management

## React Query

React Query is used for server-side data synchronization.

Responsibilities:

* API request execution
* Response caching
* Request deduplication
* Error handling
* Automatic refetching

---

## Query Client Configuration

Global configuration includes centralized error handling.

Behavior:

```text
Server Error
    |
React Query Mutation Error
    |
Redux Modal Dispatch
    |
Network Error Modal
```

This provides a consistent user experience for backend failures.

---

# API Layer Architecture

The frontend follows a dedicated API service architecture.

```text
api/
 -- accountApi
 --  authApi
 --  movieApi
 --  discussionApi
 --  historyApi
 -- assistantApi
```

Each module is responsible for communication with a specific backend domain.

---

## Authentication API

Responsibilities:

* User registration
* User login

Endpoints:

```text
POST /register
POST /login
```

---

## Account API

Responsibilities:

* Account retrieval
* Account updates
* Account deletion
* Password reset requests

Endpoints:

```text
GET /account
PATCH /account
DELETE /account
POST /drop-password
```

---

## Movie API

Responsibilities:

* Movie catalog retrieval
* Popular movies
* Movie details

Endpoints:

```text
GET /movies
GET /movies/{id}
GET /popular
```

---

## Discussion API

Responsibilities:

* AI movie discussions
* Message retrieval
* Message submission

Endpoints:

```text
POST /discussions
GET /messages
POST /messages
```

---

## History API

Responsibilities:

* Watch history retrieval
* Watch progress tracking
* History persistence

Endpoints:

```text
GET /history
POST /history/{movieId}
```

---

## AI Assistant API

Responsibilities:

* Mood-based movie recommendations

Endpoint:

```text
POST /assistant/mood
```

---

# Authentication and Request Pipeline

All requests pass through a centralized API utility layer.

Request flow:

```text
Component
 |
API Service
 |
apiFetch()
 |
Token Injection
 |
Backend API
```

### Authentication Header Injection

If a JWT token exists:

```http
Authorization: Bearer {token}
```

is automatically attached to requests.

---

# Error Handling Architecture

A centralized error handling strategy is implemented.

## API Errors

Custom exception type:

```ts
ApiError
```

Contains:

```ts
status
message
```

---

## Error Pages

Dedicated error pages exist for:

```text
401 Unauthorized
403 Forbidden
404 Not Found
500 Server Error
```

Each page provides:

* User-friendly title
* Explanation message
* Navigation back to the application

# QA Engineer

## Testing Results and Quality Assurance Summary

### Overview

Quality assurance activities for the Viora platform focused on validating the stability, correctness, and reliability of the backend services that support authentication, content management, AI integration, streaming, verification workflows, and user history management.

Testing was primarily performed through automated unit and integration tests, with code coverage measured using JaCoCo.

---

# Overall Test Coverage Results

## Global Coverage Metrics

| Metric               | Result |
| -------------------- | ------ |
| Instruction Coverage | 73%    |
| Branch Coverage      | 56%    |
| Total Classes        | 81     |
| Tested Classes       | 67     |
| Total Methods        | 206    |
| Tested Methods       | 161    |
| Total Lines          | 598    |
| Covered Lines        | 438    |

---

## Coverage Overview

| Category     | Covered | Missed | Coverage |
| ------------ | ------- | ------ | -------- |
| Instructions | 2,098   | 758    | 73%      |
| Branches     | 49      | 37     | 56%      |
| Methods      | 161     | 45     | 78%      |
| Classes      | 67      | 14     | 83%      |

---

# Module Coverage Results

## High-Coverage Modules

The following modules achieved excellent test coverage and represent the most thoroughly validated parts of the system.

| Module                          | Instruction Coverage | Branch Coverage |
| ------------------------------- | -------------------- | --------------- |
| Account Service                 | 100%                 | 100%            |
| Auth Service                    | 100%                 | 100%            |
| Streaming Service               | 100%                 | 100%            |
| Verification Controller         | 100%                 | N/A             |
| Account Controller              | 100%                 | N/A             |
| Content Controller              | 100%                 | N/A             |
| Streaming Controller            | 100%                 | N/A             |
| Auth Controller                 | 100%                 | N/A             |
| Mail Service                    | 92%                  | N/A             |
| History Service                 | 91%                  | 100%            |
| Content Service                 | 98%                  | 93%             |
| Verification Service Strategies | 86%                  | 75%             |

---

## Medium-Coverage Modules

| Module             | Instruction Coverage | Branch Coverage |
| ------------------ | -------------------- | --------------- |
| AI Service         | 70%                  | 58%             |
| Content DTO        | 76%                  | N/A             |
| Account DTO        | 80%                  | N/A             |
| Mail Messages      | 56%                  | 50%             |
| AI Controller      | 56%                  | N/A             |
| History Controller | 56%                  | N/A             |

---

## Low-Coverage Modules

The following modules were identified as areas for future testing improvements.

| Module                     | Instruction Coverage | Branch Coverage |
| -------------------------- | -------------------- | --------------- |
| Security Configuration     | 41%                  | 0%              |
| Verification Configuration | 0%                   | 0%              |
| Utility Configuration      | 0%                   | N/A             |
| Core Configuration         | 0%                   | N/A             |
| Streaming Repository       | 0%                   | 0%              |
| Application Bootstrap      | 0%                   | N/A             |

These components mainly contain framework configuration, dependency injection definitions, or infrastructure wiring logic that provides limited business value for direct unit testing.

---

# Test Coverage by Functional Area

## Authentication Module

Coverage Status: Excellent

Tested functionality:

* User registration
* User login
* JWT generation
* JWT validation
* Authentication service logic
* Authentication controllers
* Authorization workflows

Coverage:

| Component       | Coverage |
| --------------- | -------- |
| Auth Controller | 100%     |
| Auth Service    | 100%     |
| Account Service | 100%     |

---

## Verification Module

Coverage Status: Very Good

Tested functionality:

* Email verification flow
* Password reset verification
* Verification strategy execution
* JWT verification token validation
* Verification controller endpoints

Coverage:

| Component               | Coverage |
| ----------------------- | -------- |
| Verification Controller | 100%     |
| Verification Strategies | 86%      |
| Branch Coverage         | 75%      |

---

## Content Module

Coverage Status: Excellent

Tested functionality:

* Movie retrieval
* Filtering operations
* Pagination
* Content service business logic
* REST endpoints

Coverage:

| Component          | Coverage |
| ------------------ | -------- |
| Content Service    | 98%      |
| Content Controller | 100%     |

---

## Streaming Module

Coverage Status: Excellent

Tested functionality:

* Streaming endpoints
* Streaming service logic
* Segment delivery workflows

Coverage:

| Component            | Coverage |
| -------------------- | -------- |
| Streaming Service    | 100%     |
| Streaming Controller | 100%     |

---

## History Module

Coverage Status: Excellent

Tested functionality:

* Watch history creation
* History retrieval
* Progress tracking
* User viewing records

Coverage:

| Component          | Coverage |
| ------------------ | -------- |
| History Service    | 91%      |
| History Controller | 56%      |

---

## AI Module

Coverage Status: Moderate

Tested functionality:

* AI recommendation services
* AI controller endpoints
* Prompt processing
* Movie recommendation workflows

Coverage:

| Component     | Coverage |
| ------------- | -------- |
| AI Service    | 70%      |
| AI Controller | 56%      |

Additional testing opportunities remain for external AI integration scenarios and failure handling cases.

---

# Coverage Distribution Chart

```text
100% |
 95% | Content Service
 90% | History Service
 85% | Verification Service
 80% | Account DTO
 75% | Content DTO
 70% | AI Service
 65% |
 60% |
 55% | AI Controller
 50% | Mail Messages
 45% |
 40% | Security Config
 35% |
 30% |
 25% |
 20% |
 15% |
 10% |
  5% |
  0% | Configurations / Repository Infrastructure
```

---

# Defect Prevention and Validation Activities

The testing process included validation of:

## Functional Testing

* User authentication workflows
* Registration process
* Email verification
* Password recovery
* Movie browsing and filtering
* Streaming functionality
* Watch history tracking
* AI recommendation generation

---

## API Testing

Verified:

* Request validation
* Response structures
* HTTP status codes
* Authentication requirements
* Error responses

---

## Security-Oriented Testing

Validated:

* JWT authentication mechanisms
* Authorization enforcement
* Verification token validation
* Secure password reset flow
* Protected endpoint access

---

## Error Handling Testing

Verified:

* Invalid requests
* Unauthorized access attempts
* Missing resources
* Internal server error handling
* Validation failures

---

# Testing Conclusions

The Viora MVP achieved a strong overall testing result with:

* **73% instruction coverage**
* **56% branch coverage**
* **83% class coverage**
* Full coverage of critical business services

The most important business domains—including authentication, account management, content delivery, streaming, and verification workflows—achieved high coverage levels and were extensively validated.

Areas with lower coverage are primarily infrastructure and framework configuration classes, which have limited business logic and therefore lower testing priority.

Overall, the testing results demonstrate that the implemented MVP provides a stable and reliable foundation for the core streaming and AI-assisted movie recommendation functionality planned for the Viora platform.
