package com.hrms.Human_Resource_Management_System_Back.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "address", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "street", nullable = false, length = 255)
    private String street;

    @Column(name = "zip", nullable = false, length = 20)
    private String zip;
}