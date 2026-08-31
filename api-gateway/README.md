# RailBook API Gateway

Spring Boot 4.0.7 + Spring Cloud Gateway.

Gateway: 8080
User: 8081
Train: 8082
Booking: 8083

Routes:
- /api/users/** -> http://localhost:8081
- /api/trains/** -> http://localhost:8082
- /api/bookings/** -> http://localhost:8083
