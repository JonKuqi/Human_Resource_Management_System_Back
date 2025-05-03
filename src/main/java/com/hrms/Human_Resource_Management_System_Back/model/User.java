package com.hrms.Human_Resource_Management_System_Back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

//E krijon vet tabelen ska nevoj [er
//Data is like class with constructor, getter Setter

@Data
@Entity
@Table(name = "`user`", schema="public")
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

