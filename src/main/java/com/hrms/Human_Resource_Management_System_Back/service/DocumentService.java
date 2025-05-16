package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DocumentService extends BaseService<Document, Integer>{

    private final DocumentRepository repository;

    @Override
    protected BaseRepository<Document, Integer> getRepository() {
        return repository;
    }


}
