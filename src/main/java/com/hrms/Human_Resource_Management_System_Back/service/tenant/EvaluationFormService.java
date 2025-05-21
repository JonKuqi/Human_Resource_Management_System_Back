package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;

import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationFormRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationQuestionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationFormService extends BaseService<EvaluationForm, Integer> {

    private final EvaluationFormRepository repository;
    private final EvaluationQuestionRepository questionRepository;

    public EvaluationFormService(
            EvaluationFormRepository repository,
            EvaluationQuestionRepository questionRepository
    ) {
        this.repository = repository;
        this.questionRepository = questionRepository;
    }

    @Override
    protected BaseRepository<EvaluationForm, Integer> getRepository() {
        return repository;
    }

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
