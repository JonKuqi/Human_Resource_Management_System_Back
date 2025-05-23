package com.hrms.Human_Resource_Management_System_Back.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.hrms.Human_Resource_Management_System_Back.model.UserApplication;
import com.hrms.Human_Resource_Management_System_Back.model.dto.UserApplicationDto;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller for handling user applications for job listings.
 * <p>
 * This controller exposes public endpoints for users to apply to job listings.
 * It supports multipart form submissions containing both JSON data and file uploads (e.g., CVs).
 * </p>
 */
@RestController
@RequestMapping("/api/v1/public/user-application")
@AllArgsConstructor
public class UserApplicationController extends BaseController<UserApplication, Integer> {
    /**
     * Service responsible for handling user application logic.
     */
    private final UserApplicationService service;
    /**
     * Overrides the base service getter to return the user application-specific service.
     *
     * @return the service used for managing user application entities
     */
    @Override
    protected BaseService<UserApplication, Integer> getService() {
        return service;
    }

    /**
     * Endpoint for applying to a job listing.
     * <p>
     * This method accepts a multipart/form-data request containing the user application data as a JSON string
     * and the applicant's CV file. It parses the JSON, validates the input, and stores the application and document.
     * </p>
     *
     * @param dataJson the JSON string representing {@link UserApplicationDto} with user and job details
     * @param cvFile the uploaded CV file to be attached to the application
     * @return a {@link ResponseEntity} indicating a successful application submission
     * @throws IOException if the JSON parsing or file processing fails
     */

    @Operation(
            summary = "Apply to a job listing",
            description = "Submit a user application with personal information and a CV file."
    )
    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> apply(
            @RequestPart("data") String dataJson,
            @RequestPart("cv") MultipartFile cvFile
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserApplicationDto dto = objectMapper.readValue(dataJson, UserApplicationDto.class);

        service.apply(dto, cvFile);
        return ResponseEntity.ok().build();
    }






}
