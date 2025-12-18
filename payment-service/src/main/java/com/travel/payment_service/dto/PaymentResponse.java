package com.travel.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private Double amount;
    private String status;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionId;
}
