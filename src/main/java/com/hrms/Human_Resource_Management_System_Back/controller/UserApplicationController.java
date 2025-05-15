package com.hrms.Human_Resource_Management_System_Back.controller;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.hrms.Human_Resource_Management_System_Back.model.UserApplication;
import com.hrms.Human_Resource_Management_System_Back.model.dto.UserApplicationDto;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.UserApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/public/user-application")
@AllArgsConstructor
public class UserApplicationController extends BaseController<UserApplication, Integer> {

    private final UserApplicationService service;

    @Override
    protected BaseService<UserApplication, Integer> getService() {
        return service;
    }

    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> apply(
            @RequestPart("data") UserApplicationDto dto,
            @RequestPart("cv") MultipartFile cvFile
    ) throws IOException {
        service.apply(dto, cvFile);
        return ResponseEntity.ok().build();
    }





}
