package com.railbook.bookingservice.event;

import com.railbook.bookingservice.entity.Booking;
import com.railbook.bookingservice.entity.BookingStatus;
import com.railbook.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final BookingRepository bookingRepository;

    @KafkaListener(
            topics = "payment-events",
            groupId = "booking-payment-consumer",
            properties = {
                    "spring.json.value.default.type=com.railbook.bookingservice.event.PaymentCompletedEvent",
                    "spring.json.use.type.headers=false"
            }
    )
    public void consumePaymentCompletedEvent(
            PaymentCompletedEvent event
    ) {

        log.info(
                "Received PaymentCompletedEvent: bookingId={}, paymentId={}, paymentStatus={}",
                event.bookingId(),
                event.paymentId(),
                event.paymentStatus()
        );

        Booking booking = bookingRepository
                .findById(event.bookingId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found: " + event.bookingId()
                        )
                );

        if ("SUCCESS".equalsIgnoreCase(event.paymentStatus())) {

            booking.setStatus(BookingStatus.CONFIRMED);

        } else if ("FAILED".equalsIgnoreCase(event.paymentStatus())) {

            booking.setStatus(BookingStatus.CANCELLED);

        } else {

            log.warn(
                    "Unknown payment status={} for bookingId={}",
                    event.paymentStatus(),
                    event.bookingId()
            );

            return;
        }

        Booking updatedBooking =
                bookingRepository.save(booking);

        log.info(
                "Booking updated: bookingId={}, status={}",
                updatedBooking.getId(),
                updatedBooking.getStatus()
        );
    }
}