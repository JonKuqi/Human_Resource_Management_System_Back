package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;
import com.hrms.Human_Resource_Management_System_Back.repository.BaseUserSpecificRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationFormRepository extends BaseUserSpecificRepository<EvaluationForm, Integer> {
    Optional<EvaluationForm> findByIdAndEvaluatorUserTenantId(Integer id, Integer evaluatorId);
    List<EvaluationForm> findByForUser_UserTenantIdAndStatus(Integer userTenantId, String status);

}