package com.railbook.paymentservice.event;

public record PaymentCompletedEvent(
        Long paymentId,
        Long bookingId,
        String keycloakUserId,
        String paymentStatus
) {
}