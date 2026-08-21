package com.railbook.trainservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record CreateTrainRequest(@NotBlank(message = "Train number is required") @Size(max = 20) String trainNumber,
                                 @NotBlank(message = "Train name is required") @Size(max = 150) String trainName,
                                 @NotBlank(message = "Source is required") @Size(max = 100) String source,
                                 @NotBlank(message = "Destination is required") @Size(max = 100) String destination,
                                 @NotNull(message = "Departure time is required") LocalTime departureTime,
                                 @NotNull(message = "Arrival time is required") LocalTime arrivalTime,
                                 @NotNull(message = "Total seats are required") @Min(value = 1, message = "Total seats must be at least 1") Integer totalSeats) {
}
