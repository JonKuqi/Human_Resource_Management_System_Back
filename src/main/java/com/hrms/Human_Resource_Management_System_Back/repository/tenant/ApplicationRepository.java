package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.Application;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Application} entities within tenant schemas.
 * <p>
 * This repository extends {@link BaseRepository} and provides methods for retrieving job applications
 * submitted by users, particularly filtering them by associated document IDs (e.g., CVs).
 * </p>
 */
@Repository
public interface ApplicationRepository extends BaseRepository<Application, Integer> {

    /**
     * Find all applications by the given list of document IDs.
     *
     * @param documentIds the list of document IDs
     * @return a list of applications associated with the given document IDs
     */
    List<Application> findByCv_DocumentIdIn(List<Long> documentIds);


}
