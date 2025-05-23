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

/**
 * Controller for managing job applications.
 * <p>
 * This controller handles operations related to job applications within a tenant's environment.
 * It extends the generic CRUD functionality from {@link BaseController} and provides additional endpoint
 * for filtering applications based on keywords found in uploaded CV documents.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/tenant/application")
@AllArgsConstructor
public class ApplicationController extends BaseController<Application, Integer> {

    /**
     * Service responsible for handling business logic related to applications.
     */
    private final ApplicationService svc;
    /**
     * Service responsible for filtering PDF documents based on keyword content.
     */
    private final PdfFilterService pdfFilterService;
    /**
     * Overrides the base service getter to return the application-specific service.
     *
     * @return the service used for managing application entities
     */
    @Override
    protected ApplicationService getService() {
        return svc;
    }


    /**
     * Retrieves a list of applications where the associated CV document contains any of the specified keywords.
     * <p>
     * This method uses the {@link PdfFilterService} to find documents matching any of the keywords and then
     * fetches the corresponding applications that reference those documents.
     * </p>
     *
     * @param keywords the list of keywords to search for in CV documents
     * @return a {@link ResponseEntity} containing the list of matched applications
     */
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
