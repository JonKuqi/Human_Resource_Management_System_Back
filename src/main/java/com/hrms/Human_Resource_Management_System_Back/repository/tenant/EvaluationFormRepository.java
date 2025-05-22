package com.hrms.Human_Resource_Management_System_Back.repository.tenant;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;

@Repository
public interface EvaluationFormRepository extends BaseRepository<EvaluationForm, Integer> {
}
