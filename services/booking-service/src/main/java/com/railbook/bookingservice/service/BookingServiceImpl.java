package com.railbook.bookingservice.service;

import com.railbook.bookingservice.client.TrainServiceClient;
import com.railbook.bookingservice.dto.BookingResponse;
import com.railbook.bookingservice.dto.CreateBookingRequest;
import com.railbook.bookingservice.entity.Booking;
import com.railbook.bookingservice.entity.BookingStatus;
import com.railbook.bookingservice.event.BookingCreatedEvent;
import com.railbook.bookingservice.event.BookingEventProducer;
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

    private final BookingEventProducer bookingEventProducer;

    private final BookingRepository bookingRepository;

    private final TrainServiceClient trainServiceClient;


    @Override
    @Transactional
    public BookingResponse createBooking(
            String keycloakUserId,
            CreateBookingRequest request) {

        // 1. Validate train
        trainServiceClient.getTrain(request.trainId());

        // 2. Check whether seat is already booked
        boolean seatBooked =
                bookingRepository
                        .existsByTrainIdAndJourneyDateAndSeatNumberAndStatusIn(
                                request.trainId(),
                                request.journeyDate(),
                                request.seatNumber(),
                                List.of(
                                        BookingStatus.PENDING_PAYMENT,
                                        BookingStatus.CONFIRMED
                                )
                        );

        if (seatBooked) {
            throw new SeatAlreadyBookedException(
                    "Seat " + request.seatNumber()
                            + " is already booked"
            );
        }

        // 3. Create booking
        Booking booking = Booking.builder()
                .keycloakUserId(keycloakUserId)
                .trainId(request.trainId())
                .journeyDate(request.journeyDate())
                .passengerName(request.passengerName())
                .passengerAge(request.passengerAge())
                .seatNumber(request.seatNumber())
                .status(BookingStatus.PENDING_PAYMENT)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        BookingCreatedEvent event = new BookingCreatedEvent(
                savedBooking.getId(),
                savedBooking.getKeycloakUserId(),
                savedBooking.getTrainId(),
                savedBooking.getJourneyDate(),
                savedBooking.getPassengerName(),
                savedBooking.getPassengerAge(),
                savedBooking.getSeatNumber(),
                savedBooking.getStatus().name()
        );

        bookingEventProducer.publishBookingCreated(event);

        // 4. Save
        return toResponse(
                savedBooking
        );
    }


    @Override
    public BookingResponse getBooking(
            Long id,
            String keycloakUserId) {

        return bookingRepository
                .findByIdAndKeycloakUserId(id, keycloakUserId)
                .map(this::toResponse)
                .orElseThrow(
                        () -> new BookingNotFoundException(id)
                );
    }


    @Override
    public List<BookingResponse> getBookingsByUser(
            String keycloakUserId) {

        return bookingRepository
                .findByKeycloakUserId(keycloakUserId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public void cancelBooking(
            Long id,
            String keycloakUserId) {

        Booking booking =
                bookingRepository
                        .findByIdAndKeycloakUserId(
                                id,
                                keycloakUserId
                        )
                        .orElseThrow(
                                () -> new BookingNotFoundException(id)
                        );

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);
    }


    private BookingResponse toResponse(Booking booking) {

        return new BookingResponse(
                booking.getId(),
                booking.getTrainId(),
                booking.getJourneyDate(),
                booking.getPassengerName(),
                booking.getPassengerAge(),
                booking.getSeatNumber(),
                booking.getStatus()
        );
    }
}