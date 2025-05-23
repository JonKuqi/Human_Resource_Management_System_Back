package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating a payment request.
 * <p>
 * This DTO is used when initiating a PayPal payment process from the frontend.
 * It includes the ID of the subscription being purchased, the chosen billing cycle,
 * and the preferred currency.
 * </p>
 */
@Data
@AllArgsConstructor
public class PaymentRequestDto {
    private Integer subscriptionId;
    private String billingCycle;
    private String currency;


    public PaymentRequestDto() {

    }
}
