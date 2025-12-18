package com.travel.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateRequest {
    private Double amount;
    private String paymentMethod;
}
