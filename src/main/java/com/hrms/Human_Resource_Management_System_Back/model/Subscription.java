package com.hrms.Human_Resource_Management_System_Back.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a subscription plan in the system.
 * <p>
 * Each subscription plan includes details such as the name, description, price,
 * billing cycle, and the maximum number of users allowed.
 * </p>
 */
@Entity
@Table(name = "subscription", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Integer subscriptionId;

    @Column(name = "plan_name", nullable = false, unique = true, length = 255)
    private String planName;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "billing_cycle", nullable = false, length = 50)
    private String billingCycle;

    @Column(name = "max_users")
    private Integer maxUsers;

}
