package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends BaseRepository<Document, Integer> {

//    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO document (file_name, content_type, data) VALUES (?1, ?2, ?3) RETURNING *",
            nativeQuery = true
    )
    Document insertAndReturn(String fileName, String contentType, byte[] data);


}
