package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_tenant_id", "role_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Integer userRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tenant_id", nullable = false)
    private UserTenant userTenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}