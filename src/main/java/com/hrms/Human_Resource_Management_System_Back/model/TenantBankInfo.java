package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "tenant_bank_info", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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