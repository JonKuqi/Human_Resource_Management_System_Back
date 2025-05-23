package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationQuestion;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationQuestionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing {@link EvaluationQuestion} entities.
 * <p>
 * This class extends the {@link BaseService} to provide standard CRUD operations for evaluation questions.
 * </p>
 */
@Service
public class EvaluationQuestionService extends BaseService<EvaluationQuestion, Integer> {
    /**
     * Repository for performing CRUD operations on {@link EvaluationQuestion} entities.
     */
    private final EvaluationQuestionRepository repository;

    /**
     * Constructs an {@code EvaluationQuestionService} with the required repository.
     *
     * @param repository the evaluation question repository
     */
    public EvaluationQuestionService(EvaluationQuestionRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the repository used by this service.
     *
     * @return the {@link EvaluationQuestionRepository}
     */
    @Override
    protected BaseRepository<EvaluationQuestion, Integer> getRepository() {
        return repository;
    }
}
