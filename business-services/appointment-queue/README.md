# Smart Appointment & Queue Management

Spring Boot module for customer booking, staff availability, transactional check-in queues, waitlists, reports, reminders, email, and STOMP updates. It follows the neighbouring Commerce module's Spring Boot 2.7, feature packaging, PostgreSQL/Flyway, JWT, DTO-only HTTP boundary, and Docker conventions.

## Run locally

Requirements: JDK 21-compatible runtime, Maven, Docker. Build first:

```bash
mvn -pl business-services/appointment-queue -am clean verify
docker compose -f business-services/appointment-queue/docker-compose.yml up --build
```

Swagger UI: `http://localhost:8080/swagger-ui.html`; OpenAPI: `/v3/api-docs`; health: `/actuator/health`; Mailpit: `http://localhost:8025`.

## Configuration

| Variable | Purpose | Local default |
|---|---|---|
| `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` | PostgreSQL connection | local `appointments` database/user |
| `REDIS_HOST`, `REDIS_PORT` | best-effort cache infrastructure | `localhost:6379` |
| `JWT_SECRET`, `JWT_EXPIRY_SECONDS` | HMAC signing key and lifetime | development-only key, 3600 |
| `MAIL_HOST`, `MAIL_PORT` | SMTP | `localhost:1025` |
| `CHECK_IN_WINDOW_MINUTES`, `CANCELLATION_WINDOW_MINUTES` | lifecycle windows | 60 |
| `WAITLIST_OFFER_EXPIRY_MINUTES`, `WAITLIST_SCAN_MS` | offer handling | 30, 60000 |
| `REMINDER_WINDOW_HOURS`, `REMINDER_SCAN_MS` | reminders | 24, 300000 |

Never use the checked-in development passwords or signing key in production.

## API flow

1. `POST /api/auth/register` with `{"name":"Ada","email":"ada@example.org","password":"long-password"}`.
2. `POST /api/auth/login`; send `Authorization: Bearer <accessToken>` thereafter.
3. An admin creates `/api/admin/services` and `/api/admin/staff`, assigns a service, then records `/api/staff/{id}/availability`.
4. A customer searches `GET /api/slots?serviceId=1&date=2026-08-10`, books with `POST /api/appointments`, and checks in with `POST /api/appointments/{id}/check-in`.
5. Staff use `/api/staff/queue/next`; assigned staff or admin completes through `/api/appointments/{id}/complete`.

Public STOMP queue events contain only staff/service/token metadata. Connect at `/ws` and subscribe to `/topic/queue/{staffId}` or `/topic/queue/service/{serviceId}`. PostgreSQL is authoritative; queue selection and token sequences are pessimistically locked, appointments carry optimistic versions, and a database slot constraint backs interval checks. Redis is not required for correctness, so an outage cannot lose bookings.

Waitlist endpoints are `POST/GET /api/waitlist`, `DELETE /api/waitlist/{id}`, and `POST /api/waitlist/{id}/accept`. Admin daily and monthly reports are under `/api/admin/reports`.
