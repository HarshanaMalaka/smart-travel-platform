package com.travel.hotel_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.travel.hotel_service.entity.Hotel;
import com.travel.hotel_service.repository.HotelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HotelServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(HotelServiceApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initDatabase(HotelRepository hotelRepository) {
        return args -> {
            if (hotelRepository.count() == 0) {
                hotelRepository.save(new Hotel(null, "Cinnamon Grand Colombo", "Colombo", 
                    180.00, 40, "Luxury beachfront hotel in Colombo", 5));
                hotelRepository.save(new Hotel(null, "Bangkok Marriott", "Bangkok", 
                    220.00, 35, "Modern hotel in Bangkok city center", 4));
                hotelRepository.save(new Hotel(null, "Taj Mahal Palace", "Delhi", 
                    350.00, 25, "Historic luxury hotel in Delhi", 5));
                hotelRepository.save(new Hotel(null, "Shinchon Ever8 Serviced Residence", "Seoul", 
                    150.00, 50, "Comfortable serviced residence in Seoul", 4));
                System.out.println("Hotel Service: Sample hotels created");
            } else {
                System.out.println("Hotel Service: Sample hotels already exist");
            }
        };
    }
}
