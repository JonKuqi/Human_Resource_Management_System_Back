package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.TestEntity;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import com.hrms.Human_Resource_Management_System_Back.service.TestEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test-entities")
@RequiredArgsConstructor
public class TestEntityController extends BaseController<TestEntity, Integer> {

    private final TestEntityService testEntityService;

    @Override
    protected BaseService<TestEntity, Integer> getService() {
        return testEntityService;
    }
}
