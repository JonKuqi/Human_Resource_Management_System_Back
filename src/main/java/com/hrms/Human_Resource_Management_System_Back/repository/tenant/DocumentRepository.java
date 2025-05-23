package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing {@link Document} entities within tenant schemas.
 * <p>
 * This repository extends {@link BaseRepository} and provides CRUD operations for document data,
 * such as CVs and other attachments associated with job applications.
 * </p>
 */
public interface DocumentRepository extends BaseRepository<Document, Integer> {


}
