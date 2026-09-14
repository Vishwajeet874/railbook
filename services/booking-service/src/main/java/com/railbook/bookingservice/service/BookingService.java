package com.railbook.bookingservice.service;

import com.railbook.bookingservice.dto.BookingResponse;
import com.railbook.bookingservice.dto.CreateBookingRequest;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(
            String keycloakUserId,
            CreateBookingRequest request
    );

    BookingResponse getBooking(
            Long id,
            String keycloakUserId
    );

    List<BookingResponse> getBookingsByUser(
            String keycloakUserId
    );

    void cancelBooking(
            Long id,
            String keycloakUserId
    );
}