package com.railbook.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "bookings",
        indexes = {
                @Index(
                        name = "idx_booking_keycloak_user",
                        columnList = "keycloak_user_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "keycloak_user_id", nullable = false, length = 100)
    private String keycloakUserId;

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
