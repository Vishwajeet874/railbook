package com.railbook.bookingservice.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @Qualifier("userRestClient")
    public RestClient userRestClient(
            @Value("${services.user-service.url}") String url) {

        return RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Bean
    @Qualifier("trainRestClient")
    public RestClient trainRestClient(
            @Value("${services.train-service.url}") String url) {

        return RestClient.builder()
                .baseUrl(url)
                .build();
    }
}
