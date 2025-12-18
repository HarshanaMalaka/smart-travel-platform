package com.travel.hotel_service.controller;

import com.travel.hotel_service.dto.HotelDTO;
import com.travel.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {
    
    private final HotelService hotelService;
    
    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long id) {
        HotelDTO hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }
    
    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels(
            @RequestParam(required = false) String location) {
        if (location != null && !location.isEmpty()) {
            List<HotelDTO> hotels = hotelService.getHotelsByLocation(location);
            return ResponseEntity.ok(hotels);
        }
        List<HotelDTO> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
    
    @GetMapping("/{id}/availability")
    public ResponseEntity<HotelDTO> checkAvailability(@PathVariable Long id) {
        HotelDTO hotel = hotelService.checkAvailability(id);
        return ResponseEntity.ok(hotel);
    }
    
    @PostMapping
    public ResponseEntity<HotelDTO> createHotel(@RequestBody HotelDTO hotelDTO) {
        HotelDTO createdHotel = hotelService.createHotel(hotelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HotelDTO> updateHotel(
            @PathVariable Long id,
            @RequestBody HotelDTO hotelDTO) {
        HotelDTO updatedHotel = hotelService.updateHotel(id, hotelDTO);
        return ResponseEntity.ok(updatedHotel);
    }
    
    @PutMapping("/{id}/reserve")
    public ResponseEntity<String> reserveRoom(@PathVariable Long id) {
        boolean reserved = hotelService.reserveRoom(id);
        if (reserved) {
            return ResponseEntity.ok("Room reserved successfully");
        } else {
            return ResponseEntity.badRequest().body("No rooms available");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully");
    }
}
