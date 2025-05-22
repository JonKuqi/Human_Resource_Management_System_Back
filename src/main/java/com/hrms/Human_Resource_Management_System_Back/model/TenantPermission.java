package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a tenant permission entity in the public schema of the system.
 * <p>
 * - tenantPermissionId: The unique identifier for the tenant permission.
 * - name: The name of the permission.
 * - verb: The HTTP verb associated with the permission (e.g., GET, POST).
 * - resource: The resource to which the permission applies.
 * </p>
 */
@Entity
@Table(name = "tenant_permission", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_permission_id")
    private Integer tenantPermissionId;

    @Column(name = "name")
    private String name;

    @Column(name = "verb")
    private String verb;

    @Column(name = "resource")
    private String resource;
}