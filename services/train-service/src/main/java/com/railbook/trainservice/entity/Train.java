package com.railbook.trainservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "trains", uniqueConstraints = @UniqueConstraint(name = "uk_train_number", columnNames = "train_number"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "train_number", nullable = false, unique = true, length = 20)
    private String trainNumber;
    @Column(name = "train_name", nullable = false, length = 150)
    private String trainName;
    @Column(nullable = false, length = 100)
    private String source;
    @Column(nullable = false, length = 100)
    private String destination;
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
}
