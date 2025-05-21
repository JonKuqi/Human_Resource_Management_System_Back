package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationQuestion;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationQuestionRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationQuestionService extends BaseService<EvaluationQuestion, Integer> {
    private final EvaluationQuestionRepository repository;

    public EvaluationQuestionService(EvaluationQuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    protected BaseRepository<EvaluationQuestion, Integer> getRepository() {
        return repository;
    }
}
