# RailBook Service Registry — Day 6

Eureka Service Registry for the RailBook microservices project.

## Run

```bash
mvn spring-boot:run
```

Or from IntelliJ, run `ServiceRegistryApplication`.

Eureka dashboard:

`http://localhost:8761`

Initially no services will be registered. Next we will register:
- user-service
- train-service
- booking-service
- api-gateway
