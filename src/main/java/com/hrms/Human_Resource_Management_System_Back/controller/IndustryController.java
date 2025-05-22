package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.Industry;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.IndustryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/public/industry")
@AllArgsConstructor
public class IndustryController extends BaseController<Industry, Integer>{

    private final IndustryService service;

    @Override
    protected BaseService<Industry, Integer> getService() {
        return service;
    }
}
