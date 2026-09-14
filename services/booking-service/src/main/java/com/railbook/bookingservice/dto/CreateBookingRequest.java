package com.railbook.bookingservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateBookingRequest(

        @NotNull
        Long trainId,

        @NotNull
        LocalDate journeyDate,

        @NotBlank
        String passengerName,

        @NotNull
        @Min(1)
        Integer passengerAge,

        @NotNull
        @Min(1)
        Integer seatNumber
) {
}
