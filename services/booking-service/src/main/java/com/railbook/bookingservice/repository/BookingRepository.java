package com.railbook.bookingservice.repository;

import com.railbook.bookingservice.entity.Booking;
import com.railbook.bookingservice.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByTrainIdAndJourneyDateAndSeatNumberAndStatus(Long trainId, LocalDate journeyDate, Integer seatNumber, BookingStatus status);

    List<Booking> findByKeycloakUserId(String keycloakUserId);

    Optional<Booking> findByIdAndKeycloakUserId(
            Long id,
            String keycloakUserId
    );

    void deleteByIdAndKeycloakUserId(
            Long id,
            String keycloakUserId
    );

    List<Booking> findByTrainIdAndJourneyDate(Long trainId, LocalDate journeyDate);
}
