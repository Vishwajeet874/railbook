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
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(
            topics = "booking-events",
            groupId = "payment-service"
    )
    public void consumeBookingCreatedEvent(BookingCreatedEvent event) {

        log.info(
                "Received BookingCreatedEvent: bookingId={}, userId={}",
                event.bookingId(),
                event.keycloakUserId()
        );

        if (paymentRepository.existsByBookingId(event.bookingId())) {

            log.info(
                    "Payment already exists for bookingId={}, skipping",
                    event.bookingId()
            );

            return;
        }

        // 1. Create payment
        Payment payment = Payment.builder()
                .bookingId(event.bookingId())
                .keycloakUserId(event.keycloakUserId())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        log.info(
                "Payment created: paymentId={}, status={}",
                savedPayment.getId(),
                savedPayment.getStatus()
        );

        // 2. Simulate successful payment
        savedPayment.setStatus(PaymentStatus.SUCCESS);

        Payment completedPayment =
                paymentRepository.save(savedPayment);

        log.info(
                "Payment completed: paymentId={}, status={}",
                completedPayment.getId(),
                completedPayment.getStatus()
        );

        // 3. Publish result
        PaymentCompletedEvent paymentCompletedEvent =
                new PaymentCompletedEvent(
                        completedPayment.getId(),
                        completedPayment.getBookingId(),
                        completedPayment.getKeycloakUserId(),
                        completedPayment.getStatus().name()
                );

        paymentEventProducer.publishPaymentCompleted(
                paymentCompletedEvent
        );
    }
}
