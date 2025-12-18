package com.travel.payment_service.service;

import com.travel.payment_service.dto.PaymentRequest;
import com.travel.payment_service.dto.PaymentResponse;
import com.travel.payment_service.entity.Payment;
import com.travel.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final WebClient.Builder webClientBuilder;
    
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Processing payment for booking ID: {}", request.getBookingId());
        
        // Create payment record
        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setAmount(request.getAmount());
        payment.setStatus("PENDING");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(request.getPaymentMethod() != null ? 
            request.getPaymentMethod() : "CREDIT_CARD");
        payment.setTransactionId(UUID.randomUUID().toString());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // Simulate payment processing
        boolean paymentSuccess = simulatePaymentGateway(savedPayment);
        
        if (paymentSuccess) {
            savedPayment.setStatus("COMPLETED");
            paymentRepository.save(savedPayment);
            
            // Update booking status via WebClient to Booking Service
            try {
                WebClient webClient = webClientBuilder.build();
                webClient.put()
                    .uri("http://localhost:8087/api/bookings/{id}/confirm", 
                        request.getBookingId())
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> 
                        log.info("Booking {} confirmed successfully", request.getBookingId()))
                    .doOnError(error -> 
                        log.error("Error confirming booking: {}", error.getMessage()))
                    .subscribe();
                
            } catch (Exception e) {
                log.error("Error calling booking service: {}", e.getMessage());
            }
            
        } else {
            savedPayment.setStatus("FAILED");
            paymentRepository.save(savedPayment);
        }
        
        return convertToDTO(savedPayment);
    }
    
    private boolean simulatePaymentGateway(Payment payment) {
        // Simulate payment processing delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate 95% success rate
        return Math.random() < 0.95;
    }
    
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return convertToDTO(payment);
    }
    
    public List<PaymentResponse> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new RuntimeException(
                "Payment not found with transaction ID: " + transactionId));
        return convertToDTO(payment);
    }
    
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private PaymentResponse convertToDTO(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getBookingId(),
            payment.getAmount(),
            payment.getStatus(),
            payment.getPaymentDate(),
            payment.getPaymentMethod(),
            payment.getTransactionId()
        );
    }
}
