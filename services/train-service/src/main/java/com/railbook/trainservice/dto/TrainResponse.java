package com.railbook.trainservice.dto;

import java.time.LocalTime;

public record TrainResponse(Long id, String trainNumber, String trainName, String source, String destination,
                            LocalTime departureTime, LocalTime arrivalTime, Integer totalSeats) {
}
