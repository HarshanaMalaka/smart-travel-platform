package com.travel.flight_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.travel.flight_service.entity.Flight;
import com.travel.flight_service.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class FlightServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(FlightServiceApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initDatabase(FlightRepository flightRepository) {
        return args -> {
            if (flightRepository.count() == 0) {
                flightRepository.save(new Flight(null, "FL101", "Colombo", "Dubai", 
                    LocalDate.of(2025, 2, 10), 450.00, 60));
                flightRepository.save(new Flight(null, "FL202", "Singapore", "Bangkok", 
                    LocalDate.of(2025, 2, 15), 280.00, 45));
                flightRepository.save(new Flight(null, "FL303", "Mumbai", "Delhi", 
                    LocalDate.of(2025, 2, 20), 180.00, 80));
                flightRepository.save(new Flight(null, "FL404", "Tokyo", "Seoul", 
                    LocalDate.of(2025, 2, 25), 320.00, 35));
                System.out.println("Flight Service: Sample flights created");
            } else {
                System.out.println("Flight Service: Sample flights already exist");
            }
        };
    }
}
