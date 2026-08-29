package com.railbook.bookingservice.service;

import com.railbook.bookingservice.client.TrainServiceClient;
import com.railbook.bookingservice.client.UserServiceClient;
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

    private final UserServiceClient userServiceClient;
    private final TrainServiceClient trainServiceClient;

    @Override
    @Transactional
    public BookingResponse createBooking(
            CreateBookingRequest request) {

        // 1. Validate user
        userServiceClient.getUser(request.userId());

        // 2. Validate train
        trainServiceClient.getTrain(request.trainId());

        // 3. Check seat
        boolean seatBooked =
                bookingRepository
                        .existsByTrainIdAndJourneyDateAndSeatNumberAndStatus(
                                request.trainId(),
                                request.journeyDate(),
                                request.seatNumber(),
                                BookingStatus.CONFIRMED
                        );

        if (seatBooked) {
            throw new SeatAlreadyBookedException(
                    "Seat " + request.seatNumber()
                            + " is already booked"
            );
        }

        // 4. Create booking
        Booking booking = Booking.builder()
                .userId(request.userId())
                .trainId(request.trainId())
                .journeyDate(request.journeyDate())
                .passengerName(request.passengerName())
                .passengerAge(request.passengerAge())
                .seatNumber(request.seatNumber())
                .status(BookingStatus.CONFIRMED)
                .build();

        return toResponse(
                bookingRepository.save(booking)
        );
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
