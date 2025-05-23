package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing the response of a payment creation request.
 * <p>
 * This DTO is returned to the frontend after successfully initiating a PayPal payment.
 * It contains the approval URL where the user should be redirected to complete the payment.
 * </p>
 */
@Data
@AllArgsConstructor
public class PaymentResponseDto {
    private String approvalUrl;
}
