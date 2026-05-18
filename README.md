# Inventory Service

A Spring Boot RESTful service for managing products and stock levels.

## Overview

This project provides a simple inventory management backend with:

- Product CRUD operations
- Stock quantity management
- Maximum stock level and reorder level settings
- Persistence via Spring Data JPA
- MySQL datasource configuration by default

## Technology Stack

- Java 17
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- MySQL connector
- H2 (runtime scope included)
- Maven

## Project Structure

- `src/main/java/com/example/inventory_service`
  - `controller` — REST controllers
  - `service` — business logic and DTO mapping
  - `dao` — repository-level data access service wrappers
  - `repository` — Spring Data JPA repositories
  - `entity` — JPA entities
  - `dto` — data transfer objects

## Getting Started

### Prerequisites

- Java 17
- Maven
- MySQL database accessible at `jdbc:mysql://localhost:3306/profile`

### Configure Database

The default database configuration is in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/profile
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

Create the `profile` database or update the connection settings to match your environment.

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

or run the packaged JAR:

```bash
java -jar target/inventory-service-0.0.1-SNAPSHOT.jar
```

The service listens on port `8080` by default.

## REST API

### Product Endpoints

Base path: `/productService/v1/products`

- `POST /productService/v1/products`
  - Create a new product
  - Request body: `ProductDto`

- `GET /productService/v1/products/{id}`
  - Get product by ID

- `GET /productService/v1/products`
  - List all products

- `PUT /productService/v1/products/{id}`
  - Update product by ID
  - Request body: `ProductDto`

- `DELETE /productService/v1/products/{id}`
  - Delete product by ID

> Note: `ProductController.delete` currently omits the `@PathVariable` annotation on the `id` parameter, so the delete endpoint may not behave correctly until fixed.

### Stock Management Endpoints

Base path: `/stockManagementService/v1/stockManagement`

- `POST /stockManagementService/v1/stockManagement/increase/{productId}?decrease={amount}`
  - Decrease stock quantity for a product

- `POST /stockManagementService/v1/stockManagement/decrease/{productId}?increase={amount}`
  - Increase stock quantity for a product

- `POST /stockManagementService/v1/stockManagement/set-max/{productId}?maxQuantity={amount}`
  - Set the maximum stock level for a product

- `POST /stockManagementService/v1/stockManagement/reorder/{productId}?reorderLevel={amount}`
  - Set the reorder threshold for a product

- `GET /stockManagementService/v1/stockManagement/{productId}`
  - Retrieve stock details for a product

- `GET /stockManagementService/v1/stockManagement/checkStock/{productId}?quantity={amount}`
  - Return `true` when available stock is greater than the requested quantity

> Note: the controller method names are inverted relative to the endpoint paths: `/increase` decreases stock and `/decrease` increases stock in the current implementation.

## Data Model

- `Product`
  - `id`, `name`, `description`, `price`, `category`

- `StockManagement`
  - `id` (shared with product), `quantity`, `updatedAt`, `reorderLevel`, `maxStockLevel`
  - One-to-one relationship with `Product`

When a product is created, the service automatically creates an associated `StockManagement` entry with:

- `quantity = 0`
- `maxStockLevel = 100`
- `reorderLevel = 10`

## Tests

Run unit tests with:

```bash
mvn test
```

## Notes

- The default MySQL configuration points to `localhost:3306/profile`.
- H2 is included as a runtime dependency, but the default configuration is MySQL.
- The package namespace in code is `com.example.inventory_service`, not `com.example.inventory-service`.
