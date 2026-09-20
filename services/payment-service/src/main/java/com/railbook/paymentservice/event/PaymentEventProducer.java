package com.railbook.paymentservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private static final String TOPIC = "payment-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentCompleted(
            PaymentCompletedEvent event
    ) {

        kafkaTemplate.send(
                TOPIC,
                event.bookingId().toString(),
                event
        );

        log.info(
                "Published PaymentCompletedEvent: bookingId={}, paymentId={}, status={}",
                event.bookingId(),
                event.paymentId(),
                event.paymentStatus()
        );
    }
}