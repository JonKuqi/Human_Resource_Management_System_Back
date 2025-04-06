package com.hrms.Human_Resource_Management_System_Back.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hrms.Human_Resource_Management_System_Back.model.types.EmploymentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_listing", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_listing_id")
    private Integer jobListingId;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "job_title", nullable = false, length = 255)
    private String jobTitle;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @Column(name = "custom_industry", length = 255)
    private String customIndustry;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "about_us", columnDefinition = "TEXT")
    private String aboutUs;

    @Column(name = "salary_from", precision = 10, scale = 2)
    private BigDecimal salaryFrom;

    @Column(name = "salary_to", precision = 10, scale = 2)
    private BigDecimal salaryTo;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}