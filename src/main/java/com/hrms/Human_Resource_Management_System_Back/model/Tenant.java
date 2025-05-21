package com.hrms.Human_Resource_Management_System_Back.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a tenant entity in the public schema of the system.
 * <p>
 * - tenantId: The unique identifier for the tenant.
 * - name: The name of the tenant.
 * - contactEmail: The contact email of the tenant.
 * - address: The address associated with the tenant.
 * - webLink: The website link of the tenant.
 * - description: A description of the tenant.
 * - createdAt: The date and time when the tenant was created.
 * - updatedAt: The date and time when the tenant was last updated.
 * - schemaName: The name of the schema used by the tenant in a multi-tenant system.
 * </p>
 */
@Entity
@Table(name = "tenant", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Integer tenantId;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "contact_email", nullable = false, unique = true, length = 255)
    private String contactEmail;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "web_link", length = 255)
    private String webLink;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name="schema_name")
    private String schemaName;
}