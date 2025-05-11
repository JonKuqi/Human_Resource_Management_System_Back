package com.hrms.Human_Resource_Management_System_Back.repository.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationQuestionRepository extends JpaRepository<EvaluationQuestion, Integer> {
    List<EvaluationQuestion> findByTemplateId(Integer templateId);
}