package com.hrms.Human_Resource_Management_System_Back.model.tenant;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.Human_Resource_Management_System_Back.model.Address;
import com.hrms.Human_Resource_Management_System_Back.model.Tenant;
import com.hrms.Human_Resource_Management_System_Back.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Represents a user tenant entity in the system.
 * <p>
 * - userTenantId: The unique identifier for the user tenant.
 * - user: The user associated with the tenant.
 * - tenant: The tenant that the user is associated with.
 * - firstName: The first name of the user tenant.
 * - lastName: The last name of the user tenant.
 * - phone: The phone number of the user tenant.
 * - gender: The gender of the user tenant.
 * - profilePhoto: The profile photo of the user tenant.
 * - address: The address associated with the user tenant.
 * - createdAt: The timestamp when the user tenant was created.
 * </p>
 */

@Entity
@Table(name = "user_tenant")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_tenant_id")
    private Integer userTenantId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "gender", length = 20)
    private String gender;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}