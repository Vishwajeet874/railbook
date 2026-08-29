package com.railbook.bookingservice.client;

import com.railbook.bookingservice.exception.TrainServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class TrainServiceClient {

    private final RestClient restClient;

    public TrainServiceClient(
            @Qualifier("trainRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public TrainResponse getTrain(Long trainId) {

        try{
            return restClient.get()
                .uri("/api/trains/{id}", trainId)
                .retrieve()
                .body(TrainResponse.class);
        }
        catch (HttpClientErrorException.NotFound e){
            throw new TrainServiceException(
                    "Train not found with id: " + trainId
            );
        }
    }

    public record TrainResponse(
            Long id,
            String trainNumber,
            String trainName,
            String source,
            String destination
    ) {
    }
}