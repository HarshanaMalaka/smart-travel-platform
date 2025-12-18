package com.travel.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
    private Long id;
    private String name;
    private String location;
    private Double pricePerNight;
    private Integer availableRooms;
    private String description;
    private Integer rating;
    private boolean available;
}
