package com.railbook.bookingservice.client;


import com.railbook.bookingservice.exception.UserServiceException;
import com.railbook.bookingservice.exception.UserServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;


@Slf4j
@Component
public class UserServiceClient {

    private final RestClient restClient;
    private int attemptCounter = 0;

    public UserServiceClient(@Qualifier("userRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Retry(name = "user-service", fallbackMethod = "userServiceFallback")
    @CircuitBreaker(name = "user-service", fallbackMethod = "userServiceFallback")
    public UserResponse getUser(Long userId) {

        log.info("Attempting to call User Service for userId: {} | Attempt count: {}", userId, ++attemptCounter);

        return restClient.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new UserServiceException("User not found with id: " + userId);
                })
                .body(UserResponse.class);
    }

    // Resilience4j fallback
    public UserResponse userServiceFallback(Long userId, Throwable throwable) {
        // If it's a 404 (UserServiceException), rethrow it instead of returning service unavailable
        if (throwable instanceof UserServiceException) {
            throw (UserServiceException) throwable;
        }

        throw new UserServiceUnavailableException("User service is currently unavailable");
    }

    public record UserResponse(
            Long id,
            String name,
            String email
    ) {
    }
}
