package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.Document;
import com.hrms.Human_Resource_Management_System_Back.service.PdfFilterService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenant/application")
@AllArgsConstructor
public class ApplicationController extends BaseController<Application, Integer> {
    private final ApplicationService svc;
    private final PdfFilterService pdfFilterService;

    @Override
    protected ApplicationService getService() {
        return svc;
    }

    @GetMapping(params = "cvKeywords")
    public ResponseEntity<List<Application>> getApplicationsByCvKeywords(
            @RequestParam("cvKeywords") List<String> keywords
    ) {
        List<Document> matchedDocs = pdfFilterService.findPdfsContainingAnyKeywords(keywords);
        List<Long> matchedDocIds = matchedDocs.stream()
                .map(Document::getDocumentId)
                .toList();
        return ResponseEntity.ok(svc.findByCvDocumentIds(matchedDocIds));
    }

}
