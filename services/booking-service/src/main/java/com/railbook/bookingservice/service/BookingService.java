package com.railbook.bookingservice.service;
import com.railbook.bookingservice.dto.*;
import java.util.List;
public interface BookingService {
 BookingResponse createBooking(CreateBookingRequest request);
 BookingResponse getBooking(Long id);
 List<BookingResponse> getBookingsByUser(Long userId);
 void cancelBooking(Long id);
}
