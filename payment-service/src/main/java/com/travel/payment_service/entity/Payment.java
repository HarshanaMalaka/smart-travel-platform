package com.travel.payment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long bookingId;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private String status; // PENDING, COMPLETED, FAILED
    
    @Column(nullable = false)
    private LocalDateTime paymentDate;
    
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, PAYPAL, etc.
    
    private String transactionId;
}