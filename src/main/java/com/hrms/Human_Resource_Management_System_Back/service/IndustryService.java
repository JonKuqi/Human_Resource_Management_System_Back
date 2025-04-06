package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Industry;
import com.hrms.Human_Resource_Management_System_Back.repository.IndustryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IndustryService extends BaseService<Industry, Integer> {

    private final IndustryRepository industryRepository;

    @Override
    protected IndustryRepository getRepository() {
        return industryRepository;
    }
}