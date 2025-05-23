package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing document-related operations.
 * <p>
 * This controller handles CRUD operations for documents associated with applications,
 * such as CVs and other attachments. It can be used for Contracts too.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/document")
@AllArgsConstructor
public class DocumentController extends BaseController<Document, Integer>{
    /**
     * Service responsible for handling document business logic.
     */
    private final DocumentService service;

    /**
     * Overrides the base service getter to return the document-specific service.
     *
     * @return the service used for managing document entities
     */
    @Override
    protected BaseService<Document, Integer> getService() {
        return service;
    }
}
