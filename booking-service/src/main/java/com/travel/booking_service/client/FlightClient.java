package com.travel.booking_service.client;

import com.travel.booking_service.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flight-service", url = "http://localhost:8083")
public interface FlightClient {
    
    @GetMapping("/api/flights/{id}/availability")
    FlightDTO checkFlightAvailability(@PathVariable("id") Long id);
    
    @GetMapping("/api/flights/{id}")
    FlightDTO getFlightById(@PathVariable("id") Long id);
}
