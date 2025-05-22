package com.hrms.Human_Resource_Management_System_Back.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a job listing entity in the public schema of the system.
 * <p>
 * - jobListingId: The unique identifier for the job listing.
 * - tenant: The tenant associated with the job listing.
 * - jobTitle: The title of the job.
 * - industry: The industry associated with the job listing.
 * - customIndustry: A custom industry name if not available from predefined options.
 * - location: The location of the job.
 * - employmentType: The type of employment (e.g., full-time, part-time).
 * - description: A detailed description of the job role.
 * - aboutUs: Information about the company offering the job.
 * - salaryFrom: The minimum salary for the job.
 * - salaryTo: The maximum salary for the job.
 * - validUntil: The expiration date for the job listing.
 * - createdAt: The date and time when the job listing was created.
 * </p>
 */
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

//    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private String employmentType;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}