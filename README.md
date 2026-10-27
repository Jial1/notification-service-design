# Notification Service Design — V1 / V2

A Java interview exercise about interfaces, concurrency, failure isolation, and asynchronous APIs.

| | V1 | V2 |
|---|---|---|
| Interface | Java main method | HTTP REST |
| Execution | Concurrent tasks; caller waits | Background tasks; HTTP 202 |
| Result | List<DeliveryResult> | Notification ID + GET status |
| Storage | In memory | PostgreSQL |
| Transport | Simulated providers | Simulated providers |

## Visual learning guide

**[Design Evolution: diagrams and Java code after every follow-up](docs/DESIGN_EVOLUTION.md)** — starts with Email-only, walks through Follow-ups 1–7, and includes a conceptual RabbitMQ/Outbox extension.

## V1: run
```bash
cd v1-concurrent-core
mvn test
mvn exec:java
```

## V2: run
```bash
cd v2-async-api
docker compose up -d
mvn spring-boot:run
curl -i -X POST http://localhost:8080/notifications -H 'Content-Type: application/json' -d '{"userId":"alice","message":"Hello"}'
curl http://localhost:8080/notifications/REPLACE_WITH_ID
```

Demo preferences: alice = EMAIL + SMS, bob = SMS, other users = EMAIL. Providers are simulated; nothing is actually sent.

**Scope:** V1 covers follow-ups 1–6; V2 covers follow-up 7. RabbitMQ, transactional outbox, automatic retries, and exactly-once external delivery are **not implemented**. See [DESIGN_NOTES.md](DESIGN_NOTES.md).
