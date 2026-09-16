package com.railbook.paymentservice.event;

import com.railbook.paymentservice.entity.Payment;
import com.railbook.paymentservice.entity.PaymentStatus;
import com.railbook.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {
    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "booking-events", groupId = "payment-service")
    public void consumeBookingCreatedEvent(BookingCreatedEvent event) {
        log.info("Received BookingCreatedEvent: bookingId={}, userId={}, trainId={}, seatNumber={}",
                event.bookingId(), event.keycloakUserId(), event.trainId(), event.seatNumber());
        if (paymentRepository.existsByBookingId(event.bookingId())) {
            log.info("Payment already exists for bookingId={}, skipping event", event.bookingId());
            return;
        }
        Payment payment = Payment.builder().bookingId(event.bookingId())
                .keycloakUserId(event.keycloakUserId()).status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now()).build();
        paymentRepository.save(payment);
        log.info("Payment record created for bookingId={}, status={}", event.bookingId(), PaymentStatus.PENDING);
    }
}
