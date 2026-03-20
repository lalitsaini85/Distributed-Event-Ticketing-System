package com.lalit.SeatLockSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lalit.SeatLockSystem.Model.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;
    private Double price;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    // OPTIMISTIC LOCKING:
    // This prevents two users from updating the same seat at once.
    @Version
    private Integer version;
}