# payment-service
RailBook Payment Service. Spring Boot 4.0.7, Java 21, Kafka, MySQL, Eureka.

Prerequisites: Eureka at localhost:8761, Kafka at localhost:9092, topic booking-events, MySQL.
Create database: CREATE DATABASE railbook_payment;
Update application.yml with your MySQL password.

Runs on port 8085 and registers as PAYMENT-SERVICE.
Consumes BookingCreatedEvent using consumer group payment-service and creates PENDING payment records.
