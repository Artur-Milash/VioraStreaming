# Backend Architecture — Content Module

## Overview

Модуль `content` відповідає за управління movie catalog системи:

* movie retrieval;
* movie search & filtering;
* movie metadata management;
* actor/director/writer relations;
* genre classification;
* paginated content delivery.

Архітектура реалізована через layered content architecture із розділенням:

* REST API layer;
* DTO projection layer;
* JPA persistence layer;
* specification-based filtering;
* repository abstraction;
* service orchestration.

---

# Architecture Structure

## Main Functional Areas

| Area                | Purpose                   |
| ------------------- | ------------------------- |
| Movie Catalog       | Movie storage & retrieval |
| Search Engine       | Dynamic filtering         |
| Metadata System     | Actors/directors/genres   |
| DTO Mapping         | API projections           |
| Specification Layer | Query composition         |

---

# 1. Controller Layer

---

# `MovieController`

Основний REST controller для роботи з movie content.

---

## Endpoints

| Method | Endpoint                 | Description          |
| ------ | ------------------------ | -------------------- |
| GET    | `/api/v1/movies`         | Search/filter movies |
| GET    | `/api/v1/movies/popular` | Popular movie feed   |
| GET    | `/api/v1/movies/{id}`    | Detailed movie info  |

---

# Main Responsibilities

* pagination support;
* movie search;
* movie detail retrieval;
* filter binding;
* response mapping.

---

# Pagination Support

Використовується:

```java id="x9w3kc"
Pageable
```

та:

```java id="m2p8rt"
@PageableDefault(size = 20)
```

---

# Search Flow

1. Client надсилає filter params
2. `MovieFilter` binding
3. `MovieService.searchMovies()`
4. Dynamic specification creation
5. Repository query execution
6. Paginated DTO response

---

# 2. DTO Layer

---

# Lightweight DTOs

## `MovieSummary`

Використовується для:

* movie listings;
* recommendation feeds;
* paginated catalog responses.

---

## Main Fields

| Field             | Description          |
| ----------------- | -------------------- |
| id                | Movie ID             |
| title             | Movie title          |
| poster            | Poster URL           |
| releaseDate       | Release date         |
| genres            | Movie genres         |
| rating            | IMDb/internal rating |
| durationInMinutes | Runtime              |
| plot              | Short plot           |

---

# Detailed DTOs

## `MovieDto`

Full movie representation.

---

## Includes

* actors;
* director;
* writer;
* genres;
* synopsis;
* video URL;
* IMDb ID;
* rating metadata.

---

# Supporting DTOs

| DTO           | Purpose             |
| ------------- | ------------------- |
| `ActorDto`    | Actor projection    |
| `DirectorDto` | Director projection |
| `WriterDto`   | Writer projection   |
| `GenreDto`    | Genre projection    |

---

# Filtering DTOs

## `MovieFilter`

Central filtering object.

---

## Supported Filters

| Filter      | Description        |
| ----------- | ------------------ |
| search      | Title search       |
| genresIds   | Genre filtering    |
| rating      | Minimum rating     |
| releaseYear | Release year range |
| duration    | Runtime range      |

---

# Range DTOs

## `ReleaseYear`

```java id="q4r8zt"
(from, to)
```

---

## `Duration`

```java id="c5m2yv"
(from, to)
```

---

# 3. Persistence Layer

---

# `Movie`

Core movie entity.

---

# Main Fields

| Field             | Description              |
| ----------------- | ------------------------ |
| title             | Movie title              |
| plot              | Short description        |
| synopsis          | Full synopsis            |
| poster            | Poster image             |
| rated             | Content rating           |
| rating            | Score                    |
| videoUrl          | Streaming/trailer URL    |
| releaseDate       | Release date             |
| durationInMinutes | Runtime                  |
| imdbId            | External IMDb identifier |

---

# Relationships

---

## Actors

```java id="z6x3pd"
@ManyToMany
```

### Join Table

```java id="a0v1ql"
movie_actors
```

---

## Genres

```java id="p8m2xd"
@ManyToMany
```

### Join Table

```java id="j3n7kc"
movie_genres
```

---

## Director

```java id="v5k1wa"
@ManyToOne
```

---

## Writer

```java id="d2r9ut"
@ManyToOne
```

---

# Supporting Entities

| Entity     | Purpose         |
| ---------- | --------------- |
| `Actor`    | Movie actors    |
| `Director` | Movie directors |
| `Genre`    | Genre taxonomy  |
| `Writer`   | Screenwriters   |

---

# Entity Characteristics

## Actor

* name;
* photo.

## Director

* name;
* photo.

## Writer

* name;
* photo.

## Genre

* unique genre name.

---

# 4. Repository Layer

---

# `MovieRepository`

Main persistence abstraction.

---

## Extends

```java id="y2h4qs"
JpaRepository
JpaSpecificationExecutor
```

---

# Core Repository Features

| Feature        | Purpose               |
| -------------- | --------------------- |
| CRUD           | Base persistence      |
| Specifications | Dynamic filtering     |
| Custom queries | Optimized fetching    |
| Pagination     | Large dataset support |

---

# Custom Queries

---

## `findAllWithGenres()`

### Purpose

* avoid N+1 queries;
* preload genres;
* optimized paginated feeds.

---

## `findFullMovieById()`

### Fetches

* actors;
* director;
* writer;
* genres.

### Purpose

* detailed movie view;
* eager graph loading.

---

## `findMoviesByImdbId()`

### Purpose

* external integration lookup;
* synchronization support.

---

## `findMoviesByIdIn()`

### Purpose

* batch retrieval;
* AI/recommendation integration.

---

# 5. Service Layer

---

# `MovieServiceImpl`

Основний orchestration service.

---

# Responsibilities

* movie retrieval;
* search orchestration;
* DTO mapping;
* repository coordination;
* specification execution.

---

# Main Methods

| Method                  | Purpose            |
| ----------------------- | ------------------ |
| `searchMovies()`        | Dynamic filtering  |
| `getMovies()`           | Popular feed       |
| `getMovieById()`        | Detailed movie     |
| `getMovieByImdbId()`    | External lookup    |
| `getMoviesByIds()`      | Batch retrieval    |
| `getMovieSummaryById()` | Lightweight lookup |

---

# DTO Mapping

## Mapping Strategy

### Lightweight mapping

```java id="n4s8ph"
mapToMovieSummary()
```

### Full mapping

```java id="m7q2xr"
objectMapper.convertValue()
```

---

# 6. Dynamic Filtering System

---

# `MovieSpecification`

Dynamic query builder.

---

# Purpose

* composable filtering;
* runtime query generation;
* reusable query conditions.

---

# Supported Specifications

| Specification          | Purpose         |
| ---------------------- | --------------- |
| `hasTitle()`           | Search by title |
| `hasGenres()`          | Genre filtering |
| `hasRating()`          | Minimum rating  |
| `hasReleaseYear()`     | Year range      |
| `hasDurationBetween()` | Runtime range   |

---

# Specification Composition

```java id="u5r2fd"
Specification.allOf(specs)
```

---

# Query Characteristics

## Genre Filtering

Uses:

```java id="s1p7kw"
query.distinct(true)
```

### Reason

Avoid duplicate rows after joins.

---

# Release Year Filtering

Converts:

```java id="k3v9mh"
ReleaseYear
```

into:

```java id="f0t8yw"
LocalDate range
```

---

# Duration Filtering

Supports:

* open ranges;
* partial ranges;
* default bounds.

---

# 7. Exception Handling

---

# `MovieNotFoundException`

HTTP Status:

```java id="b2n5xa"
404 NOT_FOUND
```

---

# Trigger Scenarios

| Scenario               |
| ---------------------- |
| Invalid movie ID       |
| Missing IMDb ID        |
| Missing summary lookup |

---

# 8. Main Data Flows

---

# Movie Search Flow

1. Client sends filters
2. Build specifications
3. Execute dynamic query
4. Map entities → DTOs
5. Return paginated response

---

# Detailed Movie Retrieval Flow

1. Request movie ID
2. Execute fetch-join query
3. Load full entity graph
4. Convert → `MovieDto`
5. Return detailed response

---

# Recommendation Support Flow

1. AI/history module requests IDs
2. Batch repository lookup
3. Convert → `MovieSummary`
4. Return lightweight projections

---

# 9. Key RTM Modules / References

---

## Controllers

* `content/controller/MovieController`

---

## Services

* `content/service/MovieServiceImpl`
* `content/service/MovieSpecification`

---

## DTO Contracts

* `content/dto/MovieDto`
* `content/dto/MovieSummary`
* `content/dto/MovieFilter`
* `content/dto/GenreDto`
* `content/dto/ActorDto`
* `content/dto/DirectorDto`
* `content/dto/WriterDto`
* `content/dto/Duration`
* `content/dto/ReleaseYear`

---

## Persistence

* `content/model/Movie`
* `content/model/Actor`
* `content/model/Director`
* `content/model/Genre`
* `content/model/Writer`

---

## Repositories

* `content/repository/MovieRepository`

---

## Exceptions

* `content/exception/MovieNotFoundException`
