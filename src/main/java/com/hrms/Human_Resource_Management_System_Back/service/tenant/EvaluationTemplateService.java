package com.hrms.Human_Resource_Management_System_Back.service.tenant;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationTemplate;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationTemplateRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationTemplateService extends BaseService<EvaluationTemplate, Integer> {

    private final EvaluationTemplateRepository repository;

    public EvaluationTemplateService(EvaluationTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    protected BaseRepository<EvaluationTemplate, Integer> getRepository() {
        return repository;
    }
}
