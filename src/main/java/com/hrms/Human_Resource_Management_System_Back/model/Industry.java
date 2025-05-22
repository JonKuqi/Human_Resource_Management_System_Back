package com.hrms.Human_Resource_Management_System_Back.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents an industry entity in the system.
 * <p>
 * - industryId: The unique identifier for the industry.
 * - name: The name of the industry.
 * </p>
 */
@Entity
@Table(name = "industry", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "industry_id")
    private Integer industryId;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;
}