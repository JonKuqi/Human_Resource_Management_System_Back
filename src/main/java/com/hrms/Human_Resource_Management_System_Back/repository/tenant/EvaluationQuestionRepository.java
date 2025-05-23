package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationQuestion;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link EvaluationQuestion} entities.
 * <p>
 * Extends {@link BaseRepository} to provide standard CRUD operations
 * for evaluation questions linked to evaluation forms in tenant-specific schemas.
 * </p>
 */
@Repository
public interface EvaluationQuestionRepository
        extends BaseRepository<EvaluationQuestion, Integer> {

}