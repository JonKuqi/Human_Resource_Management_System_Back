package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.JobTag;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.JobTagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/public/job-tag")
@AllArgsConstructor
public class JobTagController extends BaseController<JobTag, Integer>{

    private final JobTagService service;

    @Override
    protected BaseService<JobTag, Integer> getService() {
        return service;
    }
}
