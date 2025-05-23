package com.hrms.Human_Resource_Management_System_Back.service.tenant;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationAnswer;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationAnswerRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for business logic related to {@link EvaluationAnswer} entities.
 * <p>
 * Extends {@link BaseService} to leverage common CRUD operations and customizes
 * them for handling evaluation answers.
 * </p>
 */
@Service
public class EvaluationAnswerService extends BaseService<EvaluationAnswer, Integer> {


    /**
     * Repository used for database operations related to evaluation answers.
     */
    private final EvaluationAnswerRepository repository;

    /**
     * Constructs an instance of {@code EvaluationAnswerService} with the provided repository.
     *
     * @param repository the repository used for accessing evaluation answer data
     */
    public EvaluationAnswerService(EvaluationAnswerRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the repository instance used by this service.
     * Overrides the {@link BaseService#getRepository()} method.
     *
     * @return the {@link EvaluationAnswerRepository}
     */
    @Override
    protected BaseRepository<EvaluationAnswer, Integer> getRepository() {
        return repository;
    }
}
