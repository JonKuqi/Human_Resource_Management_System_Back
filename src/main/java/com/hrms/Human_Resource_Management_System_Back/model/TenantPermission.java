package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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