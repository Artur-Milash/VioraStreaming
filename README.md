# Deployment Guide

## System Requirements

### Software

* Docker Engine **24.x+** (we recommend the latest stable version)
* Docker Compose **v2.20+**
* Git

### Hardware

Minimal requirements:

| Resource | Minimum | Recommended |
| -------- | ------- | ----------- |
| CPU      | 2 vCPU  | 4 vCPU      |
| RAM      | 4 GB    | 8 GB        |
| Storage  | 20 GB   | 50 GB       |

---

## Project Structure

```text
 docker-compose.yml
.env
viora-streaming-core/          # Spring Boot backend
    -- src/
        --main/resources/db -  # Database migrations 
    -- build.gradle
    -- Dockerfile

viora-streaming-client/        # React frontend
    -- src/
    -- package.json
    -- Dockerfile
```

### Components

| Component  | Description                                          |
| ---------- | ---------------------------------------------------- |
| Frontend   | React + Nginx application                            |
| Backend    | Spring Boot REST API                                 |
| Database   | PostgreSQL 17                                        |
| AI Service | Ollama with `llama3.2` and `nomic-embed-text` models |

---

## Environment Configuration

Create a `.env` file in the project root:

```env
POSTGRES_DB=viora_streaming_db
POSTGRES_USER=viora_streaming_user
POSTGRES_PASSWORD=viora_streaming_password

SERVER_PORT=8080
FRONTEND_PORT=80

SPRING_PROFILES_ACTIVE=prod
JAVA_OPTS=-Xms256m -Xmx512m

REACT_APP_API_URL=http://localhost:8080
REACT_APP_ENV=production
```
You can use provided `.env.example` as template

---

## Deployment

### 1. Clone Repository

```bash
git clone https://github.com/Viora-Streaming/VioraStreaming.git
cd VioraStreaming
```

### 2. Configure Environment

Create and configure the `.env` file as described above.

### 3. Build and Start All Services

```bash
docker compose up --build -d
```

This command will:

* Start PostgreSQL database
* Build and start Spring Boot backend
* Build and start React frontend
* Start Ollama service
* Download required AI models (`llama3.2`, `nomic-embed-text`)

### 4. Verify Running Containers

```bash
docker compose ps
```

Expected services:

```text
postgres_db
viora_streaming_core
react-app
movie_ollama
```

### 5. Check Logs

```bash
docker compose logs -f
```

For a specific service:

```bash
docker compose logs -f viora_core
docker compose logs -f frontend
docker compose logs -f postgres_db
```

---

## Application URLs

| Service      | URL                                   |
| ------------ | ------------------------------------- |
| Frontend     | http://localhost                      |
| Backend API  | http://localhost:8080                 |
| Health Check | http://localhost:8080/actuator/health |
| Ollama API   | http://localhost:11434                |

---

## Database

PostgreSQL connection settings:

```text
Host: localhost
Port: 5432
Database: viora_streaming_db
Username: viora_streaming_user
Password: viora_streaming_password
```

---

## Stopping the Application

```bash
docker compose down
```

Stop and remove volumes:

```bash
docker compose down -v
```

---

## Credentials

Please contact us for credentials

---

## Troubleshooting

### Backend fails to start

Check:

```bash
docker compose logs viora_core
```

Verify PostgreSQL health:

```bash
docker compose ps
```

### Ollama models are missing

Pull manually:

```bash
docker exec -it movie_ollama ollama pull llama3.2
docker exec -it movie_ollama ollama pull nomic-embed-text
```

### Rebuild after code changes

```bash
docker compose up --build -d
```

# Project Code Style 

Google Java Style Guide: https://google.github.io/styleguide/javaguide.html

Front-End Guidelines: https://gist.github.com/stowball/6ca2fc1d868ebb049f043dbec782dd68