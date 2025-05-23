package com.hrms.Human_Resource_Management_System_Back.repository.tenant;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;

/**
 * Repository interface for managing {@link EvaluationForm} entities.
 * <p>
 * This interface extends {@link BaseRepository} to provide basic CRUD operations
 * for evaluation forms within tenant-specific schemas.
 * </p>
 */
@Repository
public interface EvaluationFormRepository extends BaseRepository<EvaluationForm, Integer> {
}
