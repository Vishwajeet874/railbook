package com.railbook.bookingservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateBookingRequest(
        @NotNull(message = "User ID is required") Long userId,
        @NotNull(message = "Train ID is required") Long trainId,
        @NotNull(message = "Journey date is required") @FutureOrPresent(message = "Journey date cannot be in the past") LocalDate journeyDate,
        @NotBlank(message = "Passenger name is required") @Size(max = 100) String passengerName,
        @NotNull(message = "Passenger age is required") @Min(1) @Max(120) Integer passengerAge,
        @NotNull(message = "Seat number is required") @Min(1) Integer seatNumber
) {
}
