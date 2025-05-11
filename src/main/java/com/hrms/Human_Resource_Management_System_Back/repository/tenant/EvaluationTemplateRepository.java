package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationTemplate;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationTemplateRepository extends BaseUserSpecificRepository<EvaluationTemplate, Integer> {

}