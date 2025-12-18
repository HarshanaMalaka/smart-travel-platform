package com.travel.notification_service.service;

import com.travel.notification_service.dto.NotificationRequest;
import com.travel.notification_service.dto.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class NotificationService {
    
    public NotificationResponse sendNotification(NotificationRequest request) {
        // Simulate notification sending
        log.info("===============================================");
        log.info("Sending {} notification", request.getType());
        log.info("Recipient: {}", request.getRecipient());
        log.info("Message: {}", request.getMessage());
        log.info("Time: {}", LocalDateTime.now());
        log.info("===============================================");
        
        // Simulate different notification types
        switch (request.getType().toUpperCase()) {
            case "EMAIL":
                return sendEmail(request);
            case "SMS":
                return sendSMS(request);
            case "PUSH":
                return sendPushNotification(request);
            default:
                return new NotificationResponse(
                    "FAILED",
                    "Unknown notification type: " + request.getType(),
                    LocalDateTime.now()
                );
        }
    }
    
    private NotificationResponse sendEmail(NotificationRequest request) {
        log.info("📧 Email sent successfully to: {}", request.getRecipient());
        return new NotificationResponse(
            "SUCCESS",
            "Email sent successfully to " + request.getRecipient(),
            LocalDateTime.now()
        );
    }
    
    private NotificationResponse sendSMS(NotificationRequest request) {
        log.info("📱 SMS sent successfully to: {}", request.getRecipient());
        return new NotificationResponse(
            "SUCCESS",
            "SMS sent successfully to " + request.getRecipient(),
            LocalDateTime.now()
        );
    }
    
    private NotificationResponse sendPushNotification(NotificationRequest request) {
        log.info("🔔 Push notification sent successfully to: {}", request.getRecipient());
        return new NotificationResponse(
            "SUCCESS",
            "Push notification sent successfully to " + request.getRecipient(),
            LocalDateTime.now()
        );
    }
    
    public NotificationResponse sendBookingConfirmation(
            String email, 
            Long bookingId, 
            Double totalCost) {
        
        String message = String.format(
            "Your booking #%d has been confirmed! Total cost: $%.2f. Thank you for choosing our service.",
            bookingId, totalCost
        );
        
        NotificationRequest request = new NotificationRequest(email, message, "EMAIL");
        return sendNotification(request);
    }
    
    public NotificationResponse sendPaymentConfirmation(
            String email, 
            Long bookingId, 
            Double amount) {
        
        String message = String.format(
            "Payment of $%.2f for booking #%d has been processed successfully.",
            amount, bookingId
        );
        
        NotificationRequest request = new NotificationRequest(email, message, "EMAIL");
        return sendNotification(request);
    }
}
