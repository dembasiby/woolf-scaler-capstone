# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Microservices-based e-commerce platform (Woolf University capstone). Six services managed as Maven modules: **product-service**, **cart-service**, **order-service**, **payment-service**, **notification-service**, and **user-service** (early stage/H2 only).

**Stack:** Java 21, Spring Boot 4.0.0, Maven, Docker

## Build Commands

```bash
# Build all services from root (skip tests for faster builds)
./mvnw clean package -DskipTests

# Build a single service
./mvnw clean package -DskipTests -pl product-service

# Run tests for a single service
./mvnw test -pl product-service

# Run a specific test class
./mvnw test -pl product-service -Dtest=ProductServiceImplTest

# Start all infrastructure + services
docker-compose up -d

# Rebuild and restart a single service container
docker-compose up -d --build product-service
```

## Service Architecture

| Service | Port | Database | Additional |
|---------|------|----------|------------|
| product-service | 8082 | MySQL (3307) | Elasticsearch (9200), Kafka producer |
| cart-service | 8084 | MongoDB (27018) | Redis (6379), Kafka producer/consumer |
| order-service | 8086 | MySQL (3309) | Kafka producer |
| payment-service | 8088 | MySQL (3310) | Stripe integration, Kafka producer/consumer |
| notification-service | 8090 | MySQL (3311) | Amazon SES, Kafka consumer |
| user-service | — | H2 (in-memory) | Minimal, early development |
| Kong API Gateway | 8000 | — | Admin API on 8001 |

**Kong routing** (declarative config in `kong/kong.yml`): Kong uses `strip_path: true`, so `/api/products/foo` becomes `/foo` when reaching the service. Controllers therefore use paths without the `/api/` prefix (e.g., `@RequestMapping("/orders")`, `@RequestMapping("/payments")`).

Routes: `/api/products`, `/api/carts`, `/api/orders`, `/api/payments`, `/api/notifications`

Kong also has a global `proxy-cache` plugin (60s TTL, GET requests, memory strategy, varies on Authorization header).

## Inter-Service Communication

**Primary event flow chain:**
Order confirmed → `order-events` → payment-service processes Stripe payment → `payment-events` → notification-service sends email via SES → `notification-events`

Kafka topics:
- `product-created`, `product-updated`, `product-deleted` — produced by product-service, consumed by its own `ProductSearchConsumer` to sync Elasticsearch
- `product-updates` — consumed by cart-service's `ProductUpdateListener` to keep cart item data fresh
- `cart-events` — produced by cart-service for downstream order processing
- `order-events` — produced by order-service (`OrderEventProducer`), consumed by payment-service (`OrderEventListener`, listens for ORDER_CONFIRMED) and notification-service (`OrderEventListener`)
- `payment-events` — produced by payment-service (`PaymentEventProducer`), consumed by notification-service (`PaymentEventListener`)
- `notification-events` — produced by notification-service (`NotificationEventProducer`) after sending notifications

## Key Architectural Patterns

- **DTO/Mapper pattern**: Each service has `dto/request/`, `dto/response/`, and `mapper/` packages. Entities never leak to API layer.
- **Repository pattern**: Spring Data JPA (product, order, payment, notification), MongoRepository (cart), ElasticsearchRepository (product search).
- **Caching**: Cart service uses Redis with Spring Cache abstraction (`@Cacheable`). Config in `RedisConfig.java`, TTL 600s.
- **Event-driven**: Kafka for async communication between services. Config classes in each service's `config/KafkaConfig.java`.
- **Soft deletion**: `BaseModel` in product-service has `isDeleted` flag.
- **Stripe integration**: Payment service uses `StripeService` for payment processing, configured via `StripeConfig` (reads `stripe.api.key` and `stripe.webhook.secret` from env vars).
- **Amazon SES integration**: Notification service uses `EmailService` with AWS SES SDK, configured via `SesConfig` (reads `AWS_REGION`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_SES_SENDER_EMAIL` from env vars).

## Service Internal Structure

Each service follows the same package layout under `com.dembasiby.<servicename>`:
```
controller/    — REST endpoints
service/       — Interfaces + Impl classes
repository/    — Spring Data repositories
model/         — JPA/Mongo entities
dto/           — request/ and response/ sub-packages
mapper/        — Entity ↔ DTO conversion
config/        — Redis, Kafka, Elasticsearch, Stripe configs
exception/     — Custom exceptions + global handler
```

## Docker & Infrastructure

Services are containerized with `eclipse-temurin:21-jre-alpine`. JARs must be built locally first (`./mvnw clean package -DskipTests`), then Docker copies from `target/*.jar`. Build before running `docker-compose up`.

All infrastructure runs via `docker-compose.yaml` on a shared `capstone-network` bridge. Environment variables are in `.env` (checked into repo with dev credentials). Services use health checks and `depends_on` with `condition: service_healthy` for startup ordering.

## Database Notes

- **product-service, order-service, payment-service, notification-service**: JPA with `spring.jpa.hibernate.ddl-auto=create-drop` — schema recreates on every restart. Product service loads seed data via `data.sql`.
- **cart-service**: MongoDB with no schema migration needed.

## Testing

Tests are currently boilerplate Spring Boot `ApplicationTests` stubs only. No unit or integration test suites exist yet. Swagger UI available at `http://localhost:{port}/swagger-ui.html` for manual API testing.
