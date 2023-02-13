# Spring Boot Microservices Project

This project is a collection of microservices based on Spring Boot 3.0 and Java 17, including services for users, products, orders, deliveries, and notifications. Communication between services is handled through Apache Kafka, and data is stored in a MySQL database.

## Services

### User Service
Provides functionality for authenticating and managing user information.

### Product Service
Handles the management of product information.

### Order Service
Responsible for managing orders placed by users.

### Delivery Service
Handles the delivery of products to customers.

### Notification Service
Sends notifications to users about the status of their orders and deliveries.

### Common Library
A shared library used by all services in the project.

## Message Communication
Apache Kafka is used for message communication between services.

## Database
Data is stored in a MySQL database.

## Running the Project
To run the project, you can use the `docker-compose.yml` file, which contains all necessary services except for the Common Library. The Common Library image must be created manually using the Dockerfile located in the `/common/` directory.

## Requirements
- Docker
- Docker Compose
- MySQL
- Apache Kafka

## Getting Started
1. Clone the repository to your local machine:
   `git clone https://github.com/lashamez/flatrock-task.git`
2. Build the Common Library Docker image: `cd common/ && docker build -t flatrock_common` .
3. Start the services using Docker Compose: `cd ../ && docker-compose up -d`
