package com.hrms.Human_Resource_Management_System_Back.service.tenant;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationAnswer;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationAnswerRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationAnswerService extends BaseService<EvaluationAnswer, Integer> {

    private final EvaluationAnswerRepository repository;

    public EvaluationAnswerService(EvaluationAnswerRepository repository) {
        this.repository = repository;
    }

    @Override
    protected BaseRepository<EvaluationAnswer, Integer> getRepository() {
        return repository;
    }
}
