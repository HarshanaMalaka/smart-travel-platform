package com.travel.flight_service.controller;

import com.travel.flight_service.dto.FlightDTO;
import com.travel.flight_service.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {
    
    private final FlightService flightService;
    
    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        FlightDTO flight = flightService.getFlightById(id);
        return ResponseEntity.ok(flight);
    }
    
    @GetMapping
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        List<FlightDTO> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }
    
    @GetMapping("/{id}/availability")
    public ResponseEntity<FlightDTO> checkAvailability(@PathVariable Long id) {
        FlightDTO flight = flightService.checkAvailability(id);
        return ResponseEntity.ok(flight);
    }
    
    @PostMapping
    public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightDTO flightDTO) {
        FlightDTO createdFlight = flightService.createFlight(flightDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(
            @PathVariable Long id,
            @RequestBody FlightDTO flightDTO) {
        FlightDTO updatedFlight = flightService.updateFlight(id, flightDTO);
        return ResponseEntity.ok(updatedFlight);
    }
    
    @PutMapping("/{id}/reserve")
    public ResponseEntity<String> reserveSeat(@PathVariable Long id) {
        boolean reserved = flightService.reserveSeat(id);
        if (reserved) {
            return ResponseEntity.ok("Seat reserved successfully");
        } else {
            return ResponseEntity.badRequest().body("No seats available");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
