package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "industry", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "industry_id")
    private Integer industryId;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;
}