# Design Notes / Interview Walkthrough

## V1: responsibilities
- `NotificationChannel` abstracts **how** to send through one provider. Adding Push means adding an implementation and registering it.
- `UserPreferenceRepository` abstracts **which** channels the user wants.
- `NotificationService` coordinates per-channel tasks and collects `DeliveryResult`.
- Submit **all** tasks before calling `Future.get()`. Submitting and immediately waiting inside one loop serializes the work.
- Catch provider exceptions *inside each Callable* to ensure Email failure does not cancel SMS.
- The combined V1 response still waits for the slowest provider.

## V2: asynchronous REST
- POST creates a notification and one delivery row per selected channel, then returns `202 Accepted` with a notification ID.
- An after-commit callback submits delivery IDs to a bounded `ExecutorService`. There is no `Future.get()` on the request path.
- Workers atomically claim a PENDING delivery, call the provider, and persist SUCCESS/FAILED.
- GET reads the DB rather than storing Futures. The DB is the status source of truth.
- 202 means accepted, **not** delivered. There is no guaranteed 100ms SLA.

## Requirements matrix
| Requirement | Mechanism | Scope |
|---|---|---|
| Multiple channels | Channel interface + registry | V1/V2 |
| User-selected channels | Preference repository | V1/V2 |
| Independent execution | One task per channel | V1/V2 |
| One failure doesn't stop others | Per-task exception handling | V1/V2 |
| Individual results | DeliveryResult / delivery rows | V1/V2 |
| Return immediately | No Future.get() in HTTP path | V2 |
| Retrieve status | GET by notification ID | V2 |
| Retry only failed Email | Per-delivery retry policy | Future |
| Survive crash before task submission | Transactional outbox | Future |
| Handle redelivery duplicates | Atomic claim + leases + provider idempotency | Future |

## Failure scenarios
- **DB committed; app crashes before submit:** PENDING rows remain but are not scheduled. In-process executors are not durable.
- **Worker crashes while sending:** PROCESSING may remain indefinitely without recovery.
- **Provider succeeds, app crashes before recording SUCCESS:** retry could send twice. A DB status alone cannot guarantee exactly-once external side effects.
- **Executor queue full:** rejection is logged; no recovery worker exists.
- **RabbitMQ ACK missing:** broker may redeliver; it does not prevent duplicate external sends.

Production extensions: transactional outbox, durable broker with publisher confirms/manual ACK, recovery scanner, bounded retries with exponential backoff/jitter, dead-letter queue, leases, idempotency keys, observability and authorization.
