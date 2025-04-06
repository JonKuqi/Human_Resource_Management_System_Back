package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_tag", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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