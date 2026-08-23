package com.railbook.bookingservice.service;

import com.railbook.bookingservice.dto.BookingResponse;
import com.railbook.bookingservice.dto.CreateBookingRequest;
import com.railbook.bookingservice.entity.Booking;
import com.railbook.bookingservice.entity.BookingStatus;
import com.railbook.bookingservice.exception.BookingNotFoundException;
import com.railbook.bookingservice.exception.SeatAlreadyBookedException;
import com.railbook.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest r) {
        boolean booked = bookingRepository.existsByTrainIdAndJourneyDateAndSeatNumberAndStatus(r.trainId(), r.journeyDate(), r.seatNumber(), BookingStatus.CONFIRMED);
        if (booked) throw new SeatAlreadyBookedException("Seat " + r.seatNumber() + " is already booked for train " + r.trainId() + " on " + r.journeyDate());
        Booking b = Booking.builder().userId(r.userId()).trainId(r.trainId()).journeyDate(r.journeyDate()).passengerName(r.passengerName()).passengerAge(r.passengerAge()).seatNumber(r.seatNumber()).status(BookingStatus.CONFIRMED).build();
        return toResponse(bookingRepository.save(b));
    }

    public BookingResponse getBooking(Long id) {
        return bookingRepository.findById(id).map(this::toResponse).orElseThrow(() -> new BookingNotFoundException(id));
    }

    public List<BookingResponse> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        Booking b = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
        b.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(b);
    }

    private BookingResponse toResponse(Booking b) {
        return new BookingResponse(b.getId(), b.getUserId(), b.getTrainId(), b.getJourneyDate(), b.getPassengerName(), b.getPassengerAge(), b.getSeatNumber(), b.getStatus());
    }
}
