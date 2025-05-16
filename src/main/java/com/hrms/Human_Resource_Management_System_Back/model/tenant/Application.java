package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import com.hrms.Human_Resource_Management_System_Back.model.UserGeneral;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents an application entity in the system.
 * <p>
 * - applicationId: The unique identifier for the application.
 * - jobListing: The job listing the application is associated with.
 * - userGeneral: The user general entity that applied for the job.
 * - applicantName: The name of the applicant.
 * - applicantEmail: The email address of the applicant.
 * - applicantGender: The gender of the applicant.
 * - applicantBirthDate: The birth date of the applicant.
 * - applicantPhone: The phone number of the applicant.
 * - experience: The applicant's experience related to the job.
 * - applicantComment: Any comments made by the applicant.
 * - cv: The CV of the applicant.
 * - portfolio: The portfolio of the applicant, if available.
 * - timeOfApplication: The time when the application was submitted.
 * - hrComment: Comments made by the HR department.
 * - status: The current status of the application.
 * - createdAt: The timestamp when the application was created.
 * - updatedAt: The timestamp when the application was last updated.
 * </p>
 */

@Entity
@Table(name = "application")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_listing_id", nullable = false)
    private JobListing jobListing;

    @Column(name = "applicant_name", nullable = false, length = 100)
    private String applicantName;

    @Column(name = "applicant_email", nullable = false, length = 255)
    private String applicantEmail;

    @Column(name = "applicant_gender", length = 20)
    private String applicantGender;

    @Column(name = "applicant_birth_date")
    private LocalDate applicantBirthDate;

    @Column(name = "applicant_phone", nullable = false, length = 20)
    private String applicantPhone;

    @Column(name = "experience", columnDefinition = "TEXT")
    private String experience;

    @Column(name = "applicant_comment", columnDefinition = "TEXT")
    private String applicantComment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cv_document_id")
    private Document cv;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "portfolio")
    private byte[] portfolio;

    @Column(name = "time_of_application", nullable = false)
    private LocalDateTime timeOfApplication;

    @Column(name = "hr_comment", columnDefinition = "TEXT")
    private String hrComment;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
