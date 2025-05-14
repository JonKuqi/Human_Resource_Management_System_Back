package com.hrms.Human_Resource_Management_System_Back.service;

import com.hrms.Human_Resource_Management_System_Back.model.Industry;
import com.hrms.Human_Resource_Management_System_Back.repository.IndustryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling business logic related to industries.
 * <p> This class extends {@link BaseService} and provides methods for managing industry entities, including
 * interacting with the underlying repository.</p>
 */
@Service
@AllArgsConstructor
public class IndustryService extends BaseService<Industry, Integer> {

    /**
     * The repository for performing CRUD operations on industry entities.
     */
    private final IndustryRepository industryRepository;

    /**
     * Returns the industry repository.
     * This method overrides the {@link BaseService#getRepository()} method to return the specific repository for industry entities.
     * @return the industry repository
     */
    @Override
    protected IndustryRepository getRepository() {
        return industryRepository;
    }
}