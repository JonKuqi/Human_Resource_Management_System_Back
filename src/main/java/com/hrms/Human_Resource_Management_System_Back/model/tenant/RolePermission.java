package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.Human_Resource_Management_System_Back.model.TenantPermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a role permission entity in the system.
 * <p>
 * - rolePermissionId: The unique identifier for the role permission.
 * - role: The role associated with the permission.
 * - tenantPermission: The tenant permission associated with the role.
 * - targetRoleId: The target role for which the permission applies.
 * </p>
 */
@Entity
@Table(name = "role_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_permission_id")
    private Integer rolePermissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id", nullable = false)
    private TenantPermission tenantPermission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_role_id", nullable = false)
    private Role targetRoleId;
}