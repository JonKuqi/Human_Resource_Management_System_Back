package com.hrms.Human_Resource_Management_System_Back.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents tenant bank information in the public schema of the system.
 * <p>
 * - tenantBankInfoId: The unique identifier for the tenant bank information.
 * - tenant: The tenant associated with the bank information.
 * - cardHolderName: The name of the card holder.
 * - cardLastFour: The last four digits of the card number.
 * - cardBrand: The brand of the card (e.g., Visa, MasterCard).
 * - expirationDate: The expiration date of the card.
 * - billingAddress: The billing address associated with the card.
 * - createdAt: The date and time when the tenant bank information was created.
 * </p>
 */

@Entity
@Table(name = "tenant_bank_info", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TenantBankInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_bank_info_id")
    private Integer tenantBankInfoId;

    @OneToOne
    @JoinColumn(name = "tenant_id", nullable = false, unique = true)
    private Tenant tenant;

    @Column(name = "card_holder_name", nullable = false, length = 255)
    private String cardHolderName;

    @Column(name = "card_last_four", nullable = false, length = 4)
    private String cardLastFour;

    @Column(name = "card_brand", nullable = false, length = 50)
    private String cardBrand;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "billing_address", nullable = false)
    private String billingAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}