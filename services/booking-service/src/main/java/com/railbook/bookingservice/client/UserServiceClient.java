package com.railbook.bookingservice.client;


import com.railbook.bookingservice.exception.UserServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;


@Component
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(@Qualifier("userRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

//    @CircuitBreaker(name = "userServiceBreaker", fallbackMethod = "getUserFallback")
    public UserResponse getUser(Long userId) {

        try {
            UserResponse userResponse=restClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .body(UserResponse.class);
            return userResponse;
        }
        catch (HttpClientErrorException.NotFound e) {
            throw new UserServiceException(
                    "User not found with id: " + userId
            );
        }
    }

    public record UserResponse(
            Long id,
            String name,
            String email
    ) {
    }
}
