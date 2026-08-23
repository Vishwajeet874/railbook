package com.railbook.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long trainId;
    @Column(nullable = false)
    private LocalDate journeyDate;
    @Column(nullable = false, length = 100)
    private String passengerName;
    @Column(nullable = false)
    private Integer passengerAge;
    @Column(nullable = false)
    private Integer seatNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;
}
