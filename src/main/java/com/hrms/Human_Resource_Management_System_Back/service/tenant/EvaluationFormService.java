package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;

import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationFormRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationQuestionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing {@link EvaluationForm} entities.
 * <p>
 * This service handles the creation, retrieval, updating, and deletion of evaluation forms,
 * as well as the persistence of their associated questions.
 * </p>
 */
@Service
public class EvaluationFormService extends BaseService<EvaluationForm, Integer> {

    /**
     * Repository for CRUD operations on {@link EvaluationForm}.
     */
    private final EvaluationFormRepository repository;
    /**
     * Repository for persisting related {@link com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationQuestion} entities.
     */
    private final EvaluationQuestionRepository questionRepository;

    /**
     * Constructs the EvaluationFormService with the required repositories.
     *
     * @param repository         the repository for evaluation forms
     * @param questionRepository the repository for evaluation questions
     */
    public EvaluationFormService(
            EvaluationFormRepository repository,
            EvaluationQuestionRepository questionRepository
    ) {
        this.repository = repository;
        this.questionRepository = questionRepository;
    }

    /**
     * Returns the repository used by this service.
     *
     * @return the {@link EvaluationFormRepository}
     */
    @Override
    protected BaseRepository<EvaluationForm, Integer> getRepository() {
        return repository;
    }

    /**
     * Saves an evaluation form and persists its associated questions.
     * <p>
     * Ensures that the {@code form_id} foreign key is correctly set for each question.
     * </p>
     *
     * @param entity the evaluation form to save
     * @return the saved evaluation form, including questions
     */
    @Override
    public EvaluationForm save(EvaluationForm entity) {
        EvaluationForm savedForm = repository.save(entity);

        if (entity.getQuestions() != null && !entity.getQuestions().isEmpty()) {
            entity.getQuestions().forEach(q -> q.setForm(savedForm));
            questionRepository.saveAll(entity.getQuestions());
        }
        savedForm.setQuestions(entity.getQuestions());

        return savedForm;
    }
}
