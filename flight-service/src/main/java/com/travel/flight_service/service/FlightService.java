package com.travel.flight_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.travel.flight_service.dto.FlightDTO;
import com.travel.flight_service.entity.Flight;
import com.travel.flight_service.repository.FlightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightService {
    
    private final FlightRepository flightRepository;
    
    public FlightDTO getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
        return convertToDTO(flight);
    }
    
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public FlightDTO createFlight(FlightDTO flightDTO) {
        Flight flight = convertToEntity(flightDTO);
        Flight savedFlight = flightRepository.save(flight);
        return convertToDTO(savedFlight);
    }
    
    public FlightDTO updateFlight(Long id, FlightDTO flightDTO) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
        
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        flight.setDepartureDate(flightDTO.getDepartureDate());
        flight.setPrice(flightDTO.getPrice());
        flight.setAvailableSeats(flightDTO.getAvailableSeats());
        
        Flight updatedFlight = flightRepository.save(flight);
        return convertToDTO(updatedFlight);
    }
    
    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }
    
    public FlightDTO checkAvailability(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
        
        FlightDTO flightDTO = convertToDTO(flight);
        flightDTO.setAvailable(flight.getAvailableSeats() > 0);
        return flightDTO;
    }
    
    public boolean reserveSeat(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
        
        if (flight.getAvailableSeats() <= 0) {
            return false;
        }
        
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);
        return true;
    }
    
    private FlightDTO convertToDTO(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setOrigin(flight.getOrigin());
        dto.setDestination(flight.getDestination());
        dto.setDepartureDate(flight.getDepartureDate());
        dto.setPrice(flight.getPrice());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setAvailable(flight.getAvailableSeats() > 0);
        return dto;
    }
    
    private Flight convertToEntity(FlightDTO dto) {
        Flight flight = new Flight();
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setOrigin(dto.getOrigin());
        flight.setDestination(dto.getDestination());
        flight.setDepartureDate(dto.getDepartureDate());
        flight.setPrice(dto.getPrice());
        flight.setAvailableSeats(dto.getAvailableSeats());
        return flight;
    }
}
