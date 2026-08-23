package com.railbook.bookingservice.dto;

import com.railbook.bookingservice.entity.BookingStatus;

import java.time.LocalDate;

public record BookingResponse(Long id, Long userId, Long trainId, LocalDate journeyDate, String passengerName,
                              Integer passengerAge, Integer seatNumber, BookingStatus status) {
}
