# Bank Microservices

This repository contains a set of microservices for a **Banking System** built using **Spring Boot**, **Spring Cloud**, and other essential technologies like **PostgreSQL** and **Flyway**. The project demonstrates the implementation of microservices architecture, including features such as account management, customer management, and transaction processing.

## Services in the Project:

1. **Customer Service** - Manages customer information and relationships.
2. **Account Service** - Manages accounts (creation, retrieval, balance operations) and transactions (deposits, withdrawals, transfers).
3. **Branch Service** - Manages branch-related details.
4. **API Gateway** - Acts as a single entry point for all the services.
5. **Configuration Server** - Manages configuration properties across microservices using Spring Cloud Config.

## Technologies Used:

- **Spring Boot** 3.x
- **Spring Cloud** (API Gateway, Config Server, Eureka)
- **PostgreSQL** - Database for storing application data
- **Flyway** - Database migration tool
- **Feign** - Service-to-service communication
- **Swagger** - API documentation
- **JUnit** - Unit testing framework
- **Git** - Version control

## Prerequisites:

- Java 17 or above
- PostgreSQL database
- Maven
- Git

## Setup:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/MMedaram/Bank-Microservices.git
   cd Bank-Microservices
2. **Configure PostgreSQL:**
  Set up a PostgreSQL database for each service.
  Configure the application.yml or application.properties files to point to the correct database and credentials.
3. **Run the Services:**
  Start each service independently:
  Customer Service: mvn spring-boot:run -Dspring-boot.run.profiles=customer
  Account Service: mvn spring-boot:run -Dspring-boot.run.profiles=account
  Branch Service: mvn spring-boot:run -Dspring-boot.run.profiles=branch
  API Gateway: mvn spring-boot:run -Dspring-boot.run.profiles=gateway
  Note: Make sure the services are properly configured for each individual service with the correct port and database connection.
4. **Access Swagger UI:**
  You can view the API documentation using Swagger UI at:

  Customer Service: http://localhost:8082/swagger-ui/
  Account Service: http://localhost:8083/swagger-ui/
  Branch Service: http://localhost:8081/swagger-ui/
5. **Flyway Database Migration:**
  Flyway will automatically run migrations on application startup for each service. Ensure that all SQL migration files are available in the respective service.

## **Features:**
Microservices Architecture: Each service is a standalone Spring Boot application communicating via REST APIs.
Service Discovery: Services are registered in Eureka for dynamic discovery.
API Gateway: Zuul API Gateway is used as a central entry point for all services.
Swagger: Interactive API documentation generated for each service.
Database Migrations: Flyway is used to handle database schema versioning.
