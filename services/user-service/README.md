# RailBook User Service

First microservice in the RailBook railway reservation system.

## Run

Make sure MySQL is running and update the credentials in
`src/main/resources/application.yml` if necessary.

```bash
./mvnw spring-boot:run
```

Service URL:

http://localhost:8081

## APIs

POST /api/users
GET /api/users/{id}
PUT /api/users/{id}
DELETE /api/users/{id}

Health:

GET /actuator/health

NOTE: Password hashing/authentication is intentionally not included in
this Day-1 learning implementation. Add Spring Security before using
real credentials or production data.
