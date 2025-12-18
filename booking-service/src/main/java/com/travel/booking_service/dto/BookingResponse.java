package com.travel.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long flightId;
    private Long hotelId;
    private LocalDate travelDate;
    private Double totalCost;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}
