package com.hrms.Human_Resource_Management_System_Back.model.tenant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrms.Human_Resource_Management_System_Back.model.JobListing;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a document entity within a tenant schema.
 * <p>
 * - documentId: The unique identifier of the document.
 * - fileName: The name of the file (e.g., "cv.pdf").
 * - contentType: The MIME type of the file (e.g., "application/pdf").
 * - data: The binary content of the file stored in the database.
 * </p>
 */

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

//    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", nullable = false)
    private byte[] data;


}
