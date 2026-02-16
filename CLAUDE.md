# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Microservices-based e-commerce platform (Woolf University capstone). Five services managed as Maven modules: **product-service**, **cart-service**, **order-service**, **payment-service**, and **user-service** (early stage/H2 only).

**Stack:** Java 21, Spring Boot 4.0.0, Maven, Docker

## Build Commands

```bash
# Build all services from root (skip tests for faster builds)
./mvnw clean package -DskipTests

# Build a single service
./mvnw clean package -DskipTests -pl product-service
./mvnw clean package -DskipTests -pl cart-service

# Run tests for a single service
./mvnw test -pl product-service
./mvnw test -pl cart-service

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
| user-service | — | H2 (in-memory) | Minimal, early development |
| Kong API Gateway | 8000 | — | Admin API on 8001 |

**Kong routes** (declarative config in `kong/kong.yml`):
- `/api/products` → product-service
- `/api/carts` → cart-service
- `/api/orders` → order-service
- `/api/payments` → payment-service

Kong also has a global `proxy-cache` plugin (60s TTL, GET requests, memory strategy).

## Inter-Service Communication

Kafka topics:
- `product-created`, `product-updated`, `product-deleted` — produced by product-service, consumed by product-service's `ProductSearchConsumer` to sync Elasticsearch
- `product-updates` — consumed by cart-service's `ProductUpdateListener` to keep cart item data fresh
- `cart-events` — produced by cart-service for downstream order processing
- `order-events` — produced by order-service (`OrderEventProducer`), consumed by payment-service's `OrderEventListener` (listens for ORDER_CONFIRMED to initiate Stripe payment)
- `payment-events` — produced by payment-service (`PaymentEventProducer`) after payment processing

## Key Architectural Patterns

- **DTO/Mapper pattern**: Each service has `dto/request/`, `dto/response/`, and `mapper/` packages. Entities never leak to API layer.
- **Repository pattern**: Spring Data JPA (product, order, payment), MongoRepository (cart), ElasticsearchRepository (product search).
- **Caching**: Cart service uses Redis with Spring Cache abstraction (`@Cacheable`). Config in `RedisConfig.java`, TTL 600s.
- **Event-driven**: Kafka for async communication between services. Config classes in each service's `config/KafkaConfig.java`.
- **Soft deletion**: `BaseModel` in product-service has `isDeleted` flag.
- **Stripe integration**: Payment service uses `StripeService` for payment processing, configured via `StripeConfig` (reads `stripe.api.key` and `stripe.webhook.secret` from env vars).

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

Services are containerized with `eclipse-temurin:21-jre-alpine`. JARs must be built locally first (`mvn package`), then Docker copies from `target/*.jar`.

All infrastructure runs via `docker-compose.yaml`. Environment variables are in `.env` (checked into repo with dev credentials).

## API Documentation

Swagger UI available at `http://localhost:{port}/swagger-ui.html` when services are running.

## Database Notes

- **product-service, order-service, payment-service**: JPA with `spring.jpa.hibernate.ddl-auto=create-drop` — schema recreates on every restart. Product service loads seed data via `data.sql`.
- **cart-service**: MongoDB with no schema migration needed.
