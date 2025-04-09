package com.hrms.Human_Resource_Management_System_Back.model.tenant;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;
}