package com.travel.payment_service.controller;

import com.travel.payment_service.dto.PaymentRequest;
import com.travel.payment_service.dto.PaymentResponse;
import com.travel.payment_service.dto.PaymentUpdateRequest;
import com.travel.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByBookingId(
            @PathVariable Long bookingId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByBookingId(bookingId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentResponse> getPaymentByTransactionId(
            @PathVariable String transactionId) {
        PaymentResponse payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentUpdateRequest request) {
        PaymentResponse payment = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(payment);
    }
    
    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable Long id) {
        PaymentResponse payment = paymentService.refundPayment(id);
        return ResponseEntity.ok(payment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment deleted successfully");
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Payment Service is running");
    }
}
