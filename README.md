# 🔔 Notification Service V2

A notification microservice built with **Spring Boot**, featuring JWT authentication, Kafka-based async messaging, Redis caching, and a Dead Letter Queue (DLQ) pattern for failure handling.

---

## 🏗️ Architecture Overview

```
POST /api/send
     │
     ├── Redis: Rate limit check (5 req/min per user)
     ▼
Kafka Topic: notifications
     ▼
NotificationConsumer
     ├── Save to PostgreSQL (status: Delivered)
     └── Redis: increment unread count for userId
     ▼ (on failure after 4 retries)
Kafka Topic: notifications.DLQ
     ▼
DLQConsumer → Save to PostgreSQL (status: FAILED)
```

---

## ✨ Features

- **JWT Authentication** — Stateless access tokens (15 min expiry) with refresh token rotation
- **Refresh Token Flow** — Refresh tokens stored in Redis (7-day TTL), invalidated on logout
- **Token Blacklisting** — In-memory blacklist for logged-out access tokens
- **Kafka Messaging** — Async notification delivery via Kafka topics
- **Dead Letter Queue** — Failed messages (after 3 retries) are captured in `notifications.DLQ`
- **Redis Rate Limiting** — Max 5 notifications per user per minute
- **Unread Count Cache** — Redis-backed unread notification counter per user
- **Global Exception Handling** — Centralized error responses via `@RestControllerAdvice`

---

## 🚀 Getting Started

### Prerequisites

- Docker & Docker Compose

### Run with Docker Compose

```bash
git clone <repo-url>
cd notification-service-v2
docker-compose up --build
```

This starts: **PostgreSQL**, **Redis**, **Zookeeper**, **Kafka**, and the **Spring Boot app** on port `8080`.

### Timezone (IntelliJ only)

If running locally in IntelliJ and experiencing timezone issues, add this VM option:

```
-Duser.timezone=Asia/Kolkata
```

> Run → Edit Configurations → your Spring Boot app → Modify options → Add VM options

---

## 🔐 Authentication Flow

```
LOGIN
  │
  ├─→ Generate Access Token  (JWT, 15 min, stateless)
  └─→ Generate Refresh Token (UUID, 7 days)
        └─→ Save in Redis: Key="refresh:userId", Value="uuid-token"
        └─→ Return both tokens to client

NORMAL API CALL
  Client sends Access Token → JwtAuthFilter validates → proceeds (Redis not touched)

ACCESS TOKEN EXPIRES
  Client sends Refresh Token to POST /auth/refresh
        └─→ Backend looks up Redis
        └─→ Valid  → issue new Access Token
        └─→ Invalid/Expired → 401, force re-login

LOGOUT
  └─→ Delete refresh token from Redis
  └─→ Blacklist the access token
```

---

## 📡 API Endpoints

### Auth

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/auth/register` | Register a new user | ❌ |
| `POST` | `/auth/login` | Login, receive access + refresh tokens | ❌ |
| `POST` | `/auth/refresh` | Get a new access token using refresh token | ❌ |
| `POST` | `/auth/logout` | Invalidate refresh token + blacklist access token | ✅ |

### Notifications

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/send` | Publish a notification to Kafka | ✅ |
| `GET` | `/api/my/{userId}` | Get all notifications for a user | ✅ |
| `PATCH` | `/api/{id}/read` | Mark a notification as read | ✅ |
| `GET` | `/api/unread-count/{userId}` | Get unread notification count (from Redis) | ✅ |
| `GET` | `/api/failed` | Get all failed (DLQ) notifications | ✅ |

### Sample Request Bodies

**Register / Login:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Send Notification:**
```json
{
  "userID": 1,
  "type": "IN_APP",
  "title": "New Message",
  "message": "You have a new message from Alice."
}
```

**Refresh Token:**
```json
{
  "userID": 1,
  "refreshToken": "your-refresh-token-uuid"
}
```

**Logout:**
```json
{
  "userID": 1,
  "refreshToken": "your-refresh-token-uuid",
  "token": "your-access-token-jwt"
}
```

---

## 🔄 Kafka Topics

| Topic | Purpose |
|---|---|
| `notifications` | Primary notification delivery topic |
| `notifications.DLQ` | Dead letter queue — messages that failed after 3 retries |

The consumer uses `@RetryableTopic` with 4 attempts and a 2-second backoff before routing to the DLQ.

> **Note:** Even-numbered user IDs intentionally simulate processing failures for DLQ testing.

---

## 📦 Project Structure

```
src/main/java/org/example/notificationservicev2/
├── controller/         # REST controllers (Auth, Notification)
├── dto/                # Request/Response DTOs
├── entity/             # JPA entities (User, Notification)
├── exception/          # Custom exceptions + GlobalExceptionHandler
├── kafka/              # Kafka producer, consumer, topic config
├── repository/         # Spring Data JPA repositories
├── security/           # JWT, filters, Spring Security config
└── service/            # Business logic (Auth, Notification, Redis services)
```

---

## ⚙️ Configuration

Key properties in `application.properties` (overridden by Docker Compose env vars):

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://postgres:5432/notification_db

# Redis
spring.data.redis.host=redis
spring.data.redis.port=6379

# Kafka
spring.kafka.bootstrap-servers=kafka:9092
```

---

## 🐳 Docker Services

| Service | Image | Port |
|---|---|---|
| `app` | Built from `Dockerfile` | `8080` |
| `postgres` | `postgres:16` | `5432` |
| `redis` | `redis:latest` | `6379` |
| `kafka` | `confluentinc/cp-kafka:7.4.0` | `9092` |
| `zookeeper` | `confluentinc/cp-zookeeper:7.4.0` | `2181` |

