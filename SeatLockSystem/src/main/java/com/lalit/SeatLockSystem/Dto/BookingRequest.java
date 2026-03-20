package com.lalit.SeatLockSystem.Dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long seatId;
    private Long userId;
}