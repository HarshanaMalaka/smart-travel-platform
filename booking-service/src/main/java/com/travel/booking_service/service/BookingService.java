package com.travel.booking_service.service;

import com.travel.booking_service.client.FlightClient;
import com.travel.booking_service.client.HotelClient;
import com.travel.booking_service.dto.*;
import com.travel.booking_service.entity.Booking;
import com.travel.booking_service.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final FlightClient flightClient;
    private final HotelClient hotelClient;
    private final WebClient.Builder webClientBuilder;
    
    public BookingResponse createBooking(BookingRequest request) {
        log.info("Creating booking for user: {}", request.getUserId());
        
        // 1. Validate user via WebClient
        UserDTO user = validateUser(request.getUserId());
        log.info("User validated: {}", user.getName());
        
        // 2. Check flight availability via Feign Client
        FlightDTO flight = flightClient.checkFlightAvailability(request.getFlightId());
        if (!flight.isAvailable()) {
            throw new RuntimeException("Flight not available");
        }
        log.info("Flight available: {} - {}", flight.getFlightNumber(), flight.getPrice());
        
        // 3. Check hotel availability via Feign Client
        HotelDTO hotel = hotelClient.checkHotelAvailability(request.getHotelId());
        if (!hotel.isAvailable()) {
            throw new RuntimeException("Hotel not available");
        }
        log.info("Hotel available: {} - {}", hotel.getName(), hotel.getPricePerNight());
        
        // 4. Calculate total cost
        Double totalCost = flight.getPrice() + hotel.getPricePerNight();
        log.info("Total cost calculated: {}", totalCost);
        
        // 5. Create booking with PENDING status
        Booking booking = new Booking();
        booking.setUserId(request.getUserId());
        booking.setFlightId(request.getFlightId());
        booking.setHotelId(request.getHotelId());
        booking.setTravelDate(request.getTravelDate());
        booking.setTotalCost(totalCost);
        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());
        
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created with ID: {} and status: PENDING", savedBooking.getId());
        
        // 6. Call Payment Service via WebClient
        processPayment(savedBooking.getId(), totalCost);
        
        // 7. Send notification via WebClient
        sendNotification(user.getEmail(), savedBooking.getId(), totalCost);
        
        return convertToDTO(savedBooking);
    }
    
    private UserDTO validateUser(Long userId) {
        try {
            WebClient webClient = webClientBuilder.build();
            UserDTO user = webClient.get()
                .uri("http://localhost:8082/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
            
            if (user == null || !user.isActive()) {
                throw new RuntimeException("Invalid or inactive user");
            }
            
            return user;
        } catch (Exception e) {
            log.error("Error validating user: {}", e.getMessage());
            throw new RuntimeException("User validation failed: " + e.getMessage());
        }
    }
    
    private void processPayment(Long bookingId, Double amount) {
        try {
            PaymentRequest paymentRequest = new PaymentRequest(bookingId, amount, "CREDIT_CARD");
            
            WebClient webClient = webClientBuilder.build();
            webClient.post()
                .uri("http://localhost:8085/api/payments")
                .bodyValue(paymentRequest)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> 
                    log.info("Payment initiated for booking: {}", bookingId))
                .doOnError(error -> 
                    log.error("Payment error for booking {}: {}", bookingId, error.getMessage()))
                .subscribe();
            
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
        }
    }
    
    private void sendNotification(String email, Long bookingId, Double totalCost) {
        try {
            NotificationRequest notification = new NotificationRequest(
                email,
                String.format("Your booking #%d has been created. Total cost: $%.2f", 
                    bookingId, totalCost),
                "EMAIL"
            );
            
            WebClient webClient = webClientBuilder.build();
            webClient.post()
                .uri("http://localhost:8086/api/notifications/send")
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> 
                    log.info("Notification sent for booking: {}", bookingId))
                .doOnError(error -> 
                    log.error("Notification error: {}", error.getMessage()))
                .subscribe();
            
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
        }
    }
    
    public BookingResponse confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        
        booking.setStatus("CONFIRMED");
        booking.setConfirmedAt(LocalDateTime.now());
        Booking updatedBooking = bookingRepository.save(booking);
        
        log.info("Booking {} confirmed successfully", bookingId);
        return convertToDTO(updatedBooking);
    }
    
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return convertToDTO(booking);
    }
    
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<BookingResponse> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        
        booking.setStatus("CANCELLED");
        Booking updatedBooking = bookingRepository.save(booking);
        
        log.info("Booking {} cancelled", id);
        return convertToDTO(updatedBooking);
    }
    
    private BookingResponse convertToDTO(Booking booking) {
        return new BookingResponse(
            booking.getId(),
            booking.getUserId(),
            booking.getFlightId(),
            booking.getHotelId(),
            booking.getTravelDate(),
            booking.getTotalCost(),
            booking.getStatus(),
            booking.getCreatedAt(),
            booking.getConfirmedAt()
        );
    }
}
