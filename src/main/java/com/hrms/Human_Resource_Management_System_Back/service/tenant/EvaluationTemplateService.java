package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationQuestion;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationTemplate;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationQuestionRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationTemplateRepository;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import com.hrms.Human_Resource_Management_System_Back.model.dto.CreateEvaluationTemplateRequest;
import com.hrms.Human_Resource_Management_System_Back.model.dto.InitiateEvaluationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.EvaluationFormRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EvaluationTemplateService extends BaseUserSpecificService<EvaluationTemplate, Integer> {
    private final EvaluationTemplateRepository repo;
    private final EvaluationQuestionRepository questionRepository;
    private final UserTenantRepository userTenantRepository;
    private final EvaluationFormRepository formRepository;

    @Override
    protected EvaluationTemplateRepository getRepository() {
        return repo;
    }

    public EvaluationTemplate createTemplate(CreateEvaluationTemplateRequest rq, UserTenant creator) {
        EvaluationTemplate template = EvaluationTemplate.builder()
                .title(rq.getTitle())
                .createdBy(creator)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        template = getRepository().save(template);

        List<EvaluationQuestion> questionEntities = new ArrayList<>();

        for (int i = 0; i < rq.getQuestions().size(); i++) {
            boolean isLast = i == rq.getQuestions().size() - 1;
            EvaluationQuestion q = EvaluationQuestion.builder()
                    .template(template)
                    .questionText(rq.getQuestions().get(i))
                    .comment(isLast)
                    .build();
            questionEntities.add(q);
        }

        questionRepository.saveAll(questionEntities);
        return template;
    }

    public void initiateEvaluation(InitiateEvaluationRequest rq, UserTenant hrUser) {
        UserTenant target = userTenantRepository.findById(rq.getTargetUserTenantId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        EvaluationTemplate template = repo.findById(rq.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // For now, we simulate getting evaluators manually (replace with real logic)
        List<UserTenant> evaluators = userTenantRepository.findAll();

        List<EvaluationForm> forms = new ArrayList<>();
        for (UserTenant evaluator : evaluators) {
            if (!evaluator.getUserTenantId().equals(target.getUserTenantId())) {
                EvaluationForm form = EvaluationForm.builder()
                        .template(template)
                        .forUser(target)
                        .evaluator(evaluator)
                        .status("PENDING")
                        .createdAt(LocalDateTime.now())
                        .build();
                forms.add(form);
            }
        }

        formRepository.saveAll(forms);
    }

}