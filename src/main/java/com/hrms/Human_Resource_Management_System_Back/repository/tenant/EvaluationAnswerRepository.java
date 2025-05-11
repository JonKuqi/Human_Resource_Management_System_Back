package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationAnswer;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationAnswerRepository extends BaseUserSpecificRepository<EvaluationAnswer, Integer> {
    List<EvaluationAnswer> findByFormIn(List<EvaluationForm> forms);

}