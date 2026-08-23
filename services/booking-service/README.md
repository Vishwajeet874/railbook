# RailBook Booking Service

Port: 8083
Database: railbook_booking_db

APIs:
POST   /api/bookings
GET    /api/bookings/{id}
GET    /api/bookings/user/{userId}
DELETE /api/bookings/{id}
GET    /actuator/health

Day-3 behavior: bookings are directly created as CONFIRMED after checking
for an existing CONFIRMED booking for the same train/date/seat.
User/train service validation, payment, Kafka, resilience and Saga
will be added in later stages.
