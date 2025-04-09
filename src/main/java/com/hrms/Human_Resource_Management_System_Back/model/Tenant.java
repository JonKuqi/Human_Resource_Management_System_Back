package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenant", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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