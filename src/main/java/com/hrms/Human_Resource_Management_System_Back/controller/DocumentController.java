package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/tenant/document")
@AllArgsConstructor
public class DocumentController extends BaseController<Document, Integer>{
    private final DocumentService service;


    @Override
    protected BaseService<Document, Integer> getService() {
        return service;
    }
}
