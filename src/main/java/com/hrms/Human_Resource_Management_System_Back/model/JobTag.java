package com.hrms.Human_Resource_Management_System_Back.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a job tag entity in the public schema of the system.
 * <p>
 * - jobTagId: The unique identifier for the job tag.
 * - jobListing: The job listing associated with the tag.
 * - tagName: The name of the tag.
 * </p>
 */
@Entity
@Table(name = "job_tag", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class JobTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_tag_id")
    private Integer jobTagId;

    @ManyToOne
    @JoinColumn(name = "job_listing_id", nullable = false)
    private JobListing jobListing;

    @Column(name = "tag_name", nullable = false, length = 50)
    private String tagName;
}