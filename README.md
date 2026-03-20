# SeatLock: Distributed-Event-Ticketing-System
SeatLock is a high-performance Java backend designed to handle the core challenges of event ticketing: high concurrency, data consistency, and background processing.

## 🏗 High-Level Design (HLD):
The system is built on a Modular Monolith architecture (ready to be split into Microservices) with the following flow:
1. Architectural Diagram (Flow)
-Client → REST API (Spring Boot)
-Concurrency Control: Redis (Distributed Lock) + PostgreSQL (Optimistic Locking)
-Async Processing: RabbitMQ (Task Queuing)
-Storage: PostgreSQL (ACID Transactions)

2. The Core Request Flow
-Search: User browses events (Read-only from Postgres/Redis Cache).
-Selection: User selects a seat. The system attempts a SETNX in Redis with a 10-minute TTL.
-Validation: If Redis succeeds, the DB row status is updated to LOCKED using JPA Optimistic Locking (@Version).
-Payment: Once payment is confirmed, the DB status changes to BOOKED/SOLD.
-Notification: A "Booking Success" message is pushed to RabbitMQ. The API returns a 202 Accepted to the user immediately.
-Worker: A background consumer picks up the message, generates a PDF ticket, and sends an Email.

## 🛠 Tech Stack:
-Java 17/21 & Spring Boot
-Database: PostgreSQL (Relational consistency)
-Caching: Redis (Distributed locking & TTL)
-Messaging: RabbitMQ (Asynchronous decoupling)
-Containerization: Docker & Docker Compose

## 🚀 Key Technical Features:
-Prevention of Double-Booking: Instead of using heavy table-level locks, I implemented Optimistic Locking. This allows multiple users to read seat data 
simultaneously, only failing at the final 'update' step if a conflict occurs. This significantly increases system throughput.
-Distributed Locking with Redis: To prevent "database bloat" from abandoned carts, I implemented a 10-minute "soft lock" in Redis. This ensures that seats
are temporarily reserved without committing long-term resources to the primary database until payment is initiated.
-Eventual Consistency & Async Processing: Email and PDF generation are CPU/IO intensive. By using RabbitMQ, I ensured that the User Experience (UX) remains
fast. Even if the Email service is down, the system remains functional, and notifications are sent once the service recovers.
