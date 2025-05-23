package com.hrms.Human_Resource_Management_System_Back.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


//Data is like class with constructor, getter Setter
/**
 * Represents a user entity in the public schema of the system.
 * <p>
 * - userId: The unique identifier for the user.
 * - username: The username of the user.
 * - email: The email address of the user.
 * - passwordHash: The hashed password of the user.
 * - tenantId: The tenant ID associated with the user, or null for public users.
 * - role: The role assigned to the user (e.g., USER, ADMIN).
 * - createdAt: The timestamp when the user was created.
 * </p>
 */
@Data
@Entity
@Table(name = "`user`", schema="public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User{ //makes it an Spring boot object

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    private String username;

    private String email;
    private String passwordHash;

    @Column(name="tenant_id", nullable = true)
    private Integer tenantId;


    @Column(name = "role", nullable = true)
    private String role;

    @Column(name="created_at")
    private Timestamp createdAt;

}

