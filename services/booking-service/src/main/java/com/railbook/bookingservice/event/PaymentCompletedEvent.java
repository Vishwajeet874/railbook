package com.railbook.bookingservice.event;

public record PaymentCompletedEvent(
        Long paymentId,
        Long bookingId,
        String keycloakUserId,
        String paymentStatus
) {
}