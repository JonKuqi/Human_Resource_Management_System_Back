package com.hrms.Human_Resource_Management_System_Back.service.tenant;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationFormRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class EvaluationFormService extends BaseService<EvaluationForm, Integer> {

    private final EvaluationFormRepository repository;

    public EvaluationFormService(EvaluationFormRepository repository) {
        this.repository = repository;
    }


    @Override
    protected BaseRepository<EvaluationForm, Integer> getRepository() {
        return repository;
    }
}
