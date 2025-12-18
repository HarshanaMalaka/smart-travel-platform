package com.travel.hotel_service.service;

import com.travel.hotel_service.dto.HotelDTO;
import com.travel.hotel_service.entity.Hotel;
import com.travel.hotel_service.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    
    private final HotelRepository hotelRepository;
    
    public HotelDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        return convertToDTO(hotel);
    }
    
    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<HotelDTO> getHotelsByLocation(String location) {
        return hotelRepository.findByLocation(location).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public HotelDTO createHotel(HotelDTO hotelDTO) {
        Hotel hotel = convertToEntity(hotelDTO);
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDTO(savedHotel);
    }
    
    public HotelDTO updateHotel(Long id, HotelDTO hotelDTO) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setPricePerNight(hotelDTO.getPricePerNight());
        hotel.setAvailableRooms(hotelDTO.getAvailableRooms());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setRating(hotelDTO.getRating());
        
        Hotel updatedHotel = hotelRepository.save(hotel);
        return convertToDTO(updatedHotel);
    }
    
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new RuntimeException("Hotel not found with id: " + id);
        }
        hotelRepository.deleteById(id);
    }
    
    public HotelDTO checkAvailability(Long id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        
        HotelDTO hotelDTO = convertToDTO(hotel);
        hotelDTO.setAvailable(hotel.getAvailableRooms() > 0);
        return hotelDTO;
    }
    
    public boolean reserveRoom(Long id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        
        if (hotel.getAvailableRooms() <= 0) {
            return false;
        }
        
        hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
        hotelRepository.save(hotel);
        return true;
    }
    
    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setLocation(hotel.getLocation());
        dto.setPricePerNight(hotel.getPricePerNight());
        dto.setAvailableRooms(hotel.getAvailableRooms());
        dto.setDescription(hotel.getDescription());
        dto.setRating(hotel.getRating());
        dto.setAvailable(hotel.getAvailableRooms() > 0);
        return dto;
    }
    
    private Hotel convertToEntity(HotelDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setLocation(dto.getLocation());
        hotel.setPricePerNight(dto.getPricePerNight());
        hotel.setAvailableRooms(dto.getAvailableRooms());
        hotel.setDescription(dto.getDescription());
        hotel.setRating(dto.getRating());
        return hotel;
    }
}
