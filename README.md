# 🛒 Microservices-Based E-Commerce Platform

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-blue)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker)
![AWS](https://img.shields.io/badge/AWS-deployed-orange?logo=amazon-aws)
![Kafka](https://img.shields.io/badge/Kafka-event--driven-black?logo=apache-kafka)
![License](https://img.shields.io/badge/license-educational-lightgrey)

---

## 📘 Overview
This repository contains the source code and documentation for a microservices-based e-commerce platform developed as part of an Applied Software Engineering Master’s capstone project at Woolf University (Scaler Neovarsity College).

The project demonstrates how modern backend technologies and cloud-native architectural patterns can be used to build a scalable, secure, and modular e-commerce system. The platform supports essential business capabilities such as user management, product browsing and search, cart management, order processing, payments, and notifications.

---

## 🎯 Project Objectives
- Design and implement a distributed e-commerce system using microservices  
- Apply clean architecture principles and separation of concerns  
- Use event-driven communication for loose coupling  
- Improve performance using caching and indexing  
- Implement secure authentication and authorization  
- Deploy services using AWS cloud infrastructure  
- Deliver a project suitable for academic evaluation and real-world adaptation  

---

## 🧩 System Architecture
The system follows a microservices architecture with the following key components:
- Amazon Elastic Load Balancer (ELB) for traffic distribution  
- Kong API Gateway as the single entry point  
- Independent microservices for core business domains  
- Relational and NoSQL databases  
- Apache Kafka for asynchronous messaging  
- Redis for caching  
- Elasticsearch for fast product search  

All services communicate via REST APIs and Kafka events, ensuring scalability and resilience.

---

## 🛠️ Microservices Overview
### 1️⃣ User Management Service
- User registration and login  
- Profile management  
- Password reset  
- JWT-based authentication  
- MySQL persistence  
- Kafka event publishing  

### 2️⃣ Product Catalog Service
- Product listing and browsing  
- Product categorization  
- Full-text product search  
- Elasticsearch integration  
- MySQL persistence  

### 3️⃣ Cart Service
- Add and remove items from cart  
- Cart review and quantity updates  
- MongoDB for flexible cart schema  
- Redis for fast cart retrieval  

### 4️⃣ Order Management Service
- Order creation and confirmation  
- Order history and tracking  
- Kafka-based communication with Payment and Notification services  
- MySQL persistence  

### 5️⃣ Payment Service
- Stripe payment integration  
- Transaction logging  
- Payment status management  
- Kafka events for order updates  
- MySQL persistence  

### 6️⃣ Notification Service
- Email notifications  
- Kafka consumer for system events  
- Amazon SES integration  
- Notification delivery tracking  

---

## 🔐 Security
- JWT-based authentication  
- API Gateway-level request validation  
- Secure password hashing  
- Soft deletion for data integrity  
- Role-based access control (RBAC ready)  

---

## 🚀 Technologies Used
| Category            | Technology |
|---------------------|------------|
| Backend Framework   | Spring Boot |
| Microservices       | Spring Cloud |
| API Gateway         | Kong |
| Databases           | MySQL, MongoDB |
| Cache               | Redis |
| Search Engine       | Elasticsearch |
| Messaging           | Apache Kafka |
| Payments            | Stripe |
| Notifications       | Amazon SES |
| Authentication      | JWT |
| Containerization    | Docker |
| API Documentation   | Swagger / OpenAPI |
| Diagrams            | PlantUML, draw.io |
| Cloud Platform      | AWS (EC2, VPC, RDS, ELB, Elastic Beanstalk) |

---

## 📊 Performance Optimizations
- Redis caching for cart operations  
- Indexed database queries for faster lookups  
- Elasticsearch for optimized product search  
- DTO pattern to minimize payload size  
- Soft deletion instead of physical record removal  
- Asynchronous event handling with Kafka  

---

## 📂 Project Structure (High-Level)
```
ecommerce-platform/
│
├── api-gateway/
├── user-management-service/
├── product-catalog-service/
├── cart-service/
├── order-management-service/
├── payment-service/
├── notification-service/
│
├── docker-compose.yml
├── README.md
└── docs/
├── diagrams/
└── architecture/
```

---

## 📑 API Documentation
Each service exposes Swagger/OpenAPI documentation, available once the service is running:

```
http://localhost:{port}/swagger-ui.html
```

---

## ☁️ Deployment Overview
The system is designed for deployment on Amazon Web Services (AWS):
- EC2 for compute instances  
- VPC & Security Groups for network isolation  
- Amazon RDS for relational databases  
- Redis for caching  
- Elastic Beanstalk for managed service deployment  
- Amazon SES for email delivery  

---

## 📚 Academic Context
This project was developed as part of the Master of Science in Applied Software Engineering at Woolf University (Scaler Neovarsity College). The project emphasizes:
- Distributed systems design  
- Cloud-native architecture  
- Performance optimization  
- Industry-standard backend practices  

---

## ⚠️ Limitations & Future Enhancements
- Mobile payment integration (e.g., Wave API)  
- Advanced fraud detection mechanisms  
- Centralized logging and monitoring  
- CI/CD pipeline automation  
- Frontend application integration  
- Rate limiting and circuit breaker patterns  

---

## 👨‍💻 Author
**Demba Siby**  
Master’s Student – Applied Software Engineering  
Woolf University (Scaler Neovarsity College)  
📍 Senegal  

---

## 📄 License
This project is intended for educational and research purposes.
```
