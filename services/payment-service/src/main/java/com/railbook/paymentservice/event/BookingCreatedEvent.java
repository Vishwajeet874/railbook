package com.railbook.paymentservice.event;
import java.time.LocalDate;

public record BookingCreatedEvent(
        Long bookingId,
        String keycloakUserId,
        Long trainId,
        LocalDate journeyDate,
        String passengerName,
        Integer passengerAge,
        Integer seatNumber,
        String status
) {
}