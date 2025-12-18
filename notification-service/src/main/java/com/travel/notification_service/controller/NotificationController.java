package com.travel.notification_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.travel.notification_service.dto.NotificationRequest;
import com.travel.notification_service.dto.NotificationResponse;
import com.travel.notification_service.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(
            @RequestBody NotificationRequest request) {
        
        NotificationResponse response = notificationService.sendNotification(request);
        
        if ("SUCCESS".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/booking-confirmation")
    public ResponseEntity<NotificationResponse> sendBookingConfirmation(
            @RequestParam String email,
            @RequestParam Long bookingId,
            @RequestParam Double totalCost) {
        
        NotificationResponse response = notificationService.sendBookingConfirmation(
            email, bookingId, totalCost
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/payment-confirmation")
    public ResponseEntity<NotificationResponse> sendPaymentConfirmation(
            @RequestParam String email,
            @RequestParam Long bookingId,
            @RequestParam Double amount) {
        
        NotificationResponse response = notificationService.sendPaymentConfirmation(
            email, bookingId, amount
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Notification Service is running");
    }
}
