package com.hrms.Human_Resource_Management_System_Back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


/**
 * Data Transfer Object (DTO) that represents the subscription status of a tenant.
 * <p>
 * This DTO is used to communicate whether a subscription is currently active
 * and, if applicable, the date when the subscription ends.
 * </p>
 */
@Data
@AllArgsConstructor
public class SubscriptionStatusDto {
    private boolean active;
    private LocalDate endDate;
}
