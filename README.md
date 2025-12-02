# Flight Booking System – Reactive Microservices (Spring WebFlux + Kafka + MongoDB)

A fully **reactive**, **event-driven**, **microservices-based** Flight Booking System built using **Spring Boot WebFlux**, **Reactive MongoDB**, **Kafka**, **Circuit Breaker**, and an **API Gateway**.

This project demonstrates a modern cloud-ready architecture with complete asynchronous communication between services.

---

## 1. Microservices Overview

The system is split into **two independent microservices**:

---

### Flight Service
- Manages flight inventory  
- Provides flight search APIs  
- Handles seats & availability  
- Exposes REST endpoints via WebFlux  
- Stores data in MongoDB (Reactive driver)  

---

### Booking Service
- Manages user bookings  
- Calls Flight Service using OpenFeign  
- Publishes booking events to Kafka  
- Sends booking confirmations & cancellations  
- Uses Circuit Breaker (Resilience4j) to handle failures  

---

## 2. Tech Stack

### Backend Technologies
- Java 17  
- Spring Boot
- Spring WebFlux  
- Spring Cloud Gateway  
- Spring Cloud Netflix Eureka (Service Registry)  
- Spring Cloud Circuit Breaker (Resilience4j)  
- Spring Cloud OpenFeign  
- Reactive MongoDB  
- Spring Kafka  
- Lombok  
- Maven  

---

### Reactive Framework
- Project Reactor  
- `Mono<T>` for async single values  
- `Flux<T>` for async streams  

---

### Database
- MongoDB (Reactive Driver)

---

### Message Broker
- Apache Kafka  
- Topics:
  - `booking-created`
  - `booking-cancelled`

---

### Testing Tools
- JUnit 5  
- Mockito  
- WebTestClient  
- Reactor Test  
- JaCoCo (Code Coverage ~90%)  
- SonarQube Cloud (Code Quality)  

---

### 🔹 Performance Testing
- Apache JMeter (CLI Mode)  
- Newman CLI (Postman collection runner)

---

## 3. System Features

### ✔ Microservice Architecture
Each service runs independently and communicates via HTTP + Kafka.

### ✔ Reactive Non-Blocking Design
All APIs use WebFlux + Reactor for high-throughput asynchronous processing.

---

### Flight Service Features
- Register flights  
- Update seat availability  
- Fetch flight details  
- Search flights by route / date  

---

### Booking Service Features
- Book a flight  
- Cancel a booking  
- Generate PNR  
- Fetch booking by PNR  
- Publishes Kafka events  
- Circuit breaker for Flight Service communication  

---

### Kafka Event Streams
| Topic | Purpose |
|-------|---------|
| `booking-created` | Sent when booking is confirmed |
| `booking-cancelled` | Sent when booking is cancelled |

Email service listens to both topics and sends email notifications.

---

### DTO Layer
- Controllers use DTOs instead of entities  
- Clean separation between API & persistence  
- Prevents leaking DB structures to the client  

---

### API Gateway
- Single entry point for all microservices  
- Routes to Flight Service or Booking Service  
- (Future) rate limiting, authentication, tracing  

---

### Circuit Breaker (Resilience4j)
Used to:
- Retry failed calls  
- Prevent cascading failures  
- Provide fallback responses  

---

## 4. Architecture Diagram

<img width="1525" height="779" alt="architecture-flight-microservices" src="https://github.com/user-attachments/assets/12cce77d-3014-4ae1-a62b-e12a70412426" />



