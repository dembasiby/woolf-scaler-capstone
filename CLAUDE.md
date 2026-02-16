# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Microservices-based e-commerce platform (Woolf University capstone). Three active services: **product-service**, **cart-service**, and **user-service** (early stage). Additional services (order, payment, notification) are planned but not yet implemented.

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
| cart-service | 8084 | MongoDB (27017) | Redis (6379), Kafka producer/consumer |
| user-service | — | H2 (in-memory) | Minimal, early development |
| Kong API Gateway | 8000 | — | Admin API on 8001 |

**Kong routes:** `/api/products` → product-service, `/api/carts` → cart-service (declarative config in `kong/kong.yml`).

## Inter-Service Communication

Kafka topics:
- `product-created`, `product-updated`, `product-deleted` — produced by product-service, consumed by product-service's `ProductSearchConsumer` to sync Elasticsearch
- `product-updates` — consumed by cart-service's `ProductUpdateListener` to keep cart item data fresh
- `cart-events` — produced by cart-service for downstream order processing

## Key Architectural Patterns

- **DTO/Mapper pattern**: Each service has `dto/request/`, `dto/response/`, and `mapper/` packages. Entities never leak to API layer.
- **Repository pattern**: Spring Data JPA (product), MongoRepository (cart), ElasticsearchRepository (product search).
- **Caching**: Cart service uses Redis with Spring Cache abstraction (`@Cacheable`). Config in `RedisConfig.java`, TTL 600s.
- **Event-driven**: Kafka for async communication between services. Config classes in each service's `config/KafkaConfig.java`.
- **Soft deletion**: `BaseModel` in product-service has `isDeleted` flag.

## Service Internal Structure

Each service follows the same package layout under `com.dembasiby.<servicename>`:
```
controller/    — REST endpoints
service/       — Interfaces + Impl classes
repository/    — Spring Data repositories
model/         — JPA/Mongo entities
dto/           — request/ and response/ sub-packages
mapper/        — Entity ↔ DTO conversion
config/        — Redis, Kafka, Elasticsearch configs
exception/     — Custom exceptions + global handler
```

## Docker & Infrastructure

Services are containerized with `eclipse-temurin:21-jre-alpine`. JARs must be built locally first (`mvn package`), then Docker copies from `target/*.jar`.

All infrastructure runs via `docker-compose.yaml`. Environment variables are in `.env` (checked into repo with dev credentials).

## API Documentation

Swagger UI available at `http://localhost:{port}/swagger-ui.html` when services are running.

## Database Notes

- **product-service**: JPA with `spring.jpa.hibernate.ddl-auto=create-drop` — schema recreates on every restart. Seed data loaded via `data.sql`.
- **cart-service**: MongoDB with no schema migration needed.
