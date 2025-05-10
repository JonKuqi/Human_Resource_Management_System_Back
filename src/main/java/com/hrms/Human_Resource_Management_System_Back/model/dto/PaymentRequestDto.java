package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private Integer subscriptionId;
    private String billingCycle;
    private String currency;
}
