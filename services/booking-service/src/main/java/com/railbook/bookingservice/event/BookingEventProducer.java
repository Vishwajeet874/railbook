package com.railbook.bookingservice.event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventProducer {

    private static final String TOPIC = "booking-events";

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public void publishBookingCreated(BookingCreatedEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event.bookingId().toString(),
                event
        );
    }
}