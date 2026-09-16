package com.railbook.bookingservice.event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingEventConsumer {

    @KafkaListener(
            topics = "booking-events",
            groupId = "booking-event-consumer"
    )
    public void consume(BookingCreatedEvent event) {

        log.info(
                "Received BookingCreatedEvent: bookingId={}, userId={}, trainId={}, seatNumber={}",
                event.bookingId(),
                event.keycloakUserId(),
                event.trainId(),
                event.seatNumber()
        );

//        if (event.trainId() == 1L) {
//            throw new RuntimeException("Simulated processing failure");
//        }
    }
}