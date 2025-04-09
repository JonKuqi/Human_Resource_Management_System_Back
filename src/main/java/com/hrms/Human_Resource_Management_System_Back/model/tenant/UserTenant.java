package com.hrms.Human_Resource_Management_System_Back.model.tenant;


import com.hrms.Human_Resource_Management_System_Back.model.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_tenant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_tenant_id")
    private Integer userTenantId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "gender", length = 20)
    private String gender;

    @Lob
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}