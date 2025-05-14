package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user role entity in the system.
 * <p>
 * - userRoleId: The unique identifier for the user role.
 * - userTenant: The user tenant associated with the role.
 * - role: The role assigned to the user tenant.
 * </p>
 */

@Entity
@Table(name = "user_role_table",
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