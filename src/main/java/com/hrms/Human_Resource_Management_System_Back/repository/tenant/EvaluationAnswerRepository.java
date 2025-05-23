package com.hrms.Human_Resource_Management_System_Back.repository.tenant;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationAnswer;

/**
 * Repository interface for managing {@link EvaluationAnswer} entities.
 * <p>
 * This interface extends {@link BaseRepository} to inherit basic CRUD operations
 * for working with evaluation answers stored in the tenant schema.
 * </p>
 */
@Repository
public interface EvaluationAnswerRepository extends BaseRepository<EvaluationAnswer, Integer> {
}
