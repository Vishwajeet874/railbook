package com.railbook.bookingservice.client;

import com.railbook.bookingservice.exception.TrainServiceException;
import com.railbook.bookingservice.exception.TrainServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class TrainServiceClient {

    private final RestClient restClient;
    private int attemptCounter = 0;

    public TrainServiceClient(@Qualifier("trainRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Retry(name = "train-service", fallbackMethod = "trainServiceFallback")
    @CircuitBreaker(name = "train-service", fallbackMethod = "trainServiceFallback")
    public TrainResponse getTrain(Long trainId) {

        log.info("Attempting to call Train Service for trainId: {} | Attempt count: {}", trainId, ++attemptCounter);

        return restClient.get()
                .uri("/api/trains/{id}", trainId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new TrainServiceException("Train not found with id: " + trainId);
                })
                .body(TrainResponse.class);
    }

    public TrainResponse trainServiceFallback(Long trainId, Throwable throwable) {
        // Pass through 404 exceptions directly to the caller
        if (throwable instanceof TrainServiceException) {
            throw (TrainServiceException) throwable;
        }

        log.error("Train service fallback triggered for trainId: {}. Reason: {}", trainId, throwable.getMessage());
        throw new TrainServiceUnavailableException("Train service is currently unavailable");
    }

    public record TrainResponse(
            Long id,
            String trainNumber,
            String trainName,
            String source,
            String destination
    ) {}
}