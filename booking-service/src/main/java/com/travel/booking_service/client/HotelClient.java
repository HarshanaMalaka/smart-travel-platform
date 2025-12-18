package com.travel.booking_service.client;

import com.travel.booking_service.dto.HotelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hotel-service", url = "http://localhost:8084")
public interface HotelClient {
    
    @GetMapping("/api/hotels/{id}/availability")
    HotelDTO checkHotelAvailability(@PathVariable("id") Long id);
    
    @GetMapping("/api/hotels/{id}")
    HotelDTO getHotelById(@PathVariable("id") Long id);
}
