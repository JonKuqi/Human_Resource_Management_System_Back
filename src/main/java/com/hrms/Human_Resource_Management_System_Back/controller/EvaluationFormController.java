package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.dto.EvaluationResultDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SubmitEvaluationFormRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationQuestion;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.EvaluationFormService;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tenant/evaluation-forms")
@RequiredArgsConstructor
public class EvaluationFormController extends BaseUserSpecificController<EvaluationForm, Integer> {

    private final EvaluationFormService service;
    private final UserTenantRepository userTenantRepository;
    private final EvaluationFormService formService;

    @Override
    protected EvaluationFormService getServiceSpecific() {
        return service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvaluationForm(@PathVariable Integer id) {
        Integer userTenantId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        EvaluationForm form = service.findFormForEvaluator(id, userTenantId)
                .orElseThrow(() -> new RuntimeException("Form not found or not allowed"));

        List<EvaluationQuestion> questions = service.getQuestionsForForm(form);
        UserTenant forUser = form.getForUser();

        Map<String, Object> dto = Map.of(
                "formId", form.getId(),
                "forUser", Map.of(
                        "id", forUser.getUserTenantId(),
                        "fullName", forUser.getFirstName() + " " + forUser.getLastName()
                ),
                "questions", questions.stream().map(q -> Map.of(
                        "id", q.getId(),
                        "text", q.getQuestionText(),
                        "comment", q.getComment()
                )).collect(Collectors.toList())
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitForm(@PathVariable("id") Integer formId,
                                        @RequestBody SubmitEvaluationFormRequest rq) {

        Integer userTenantId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        formService.submitForm(formId, userTenantId, rq);
        return ResponseEntity.ok(Map.of("message", "Evaluation submitted successfully"));
    }

    @GetMapping("/results/{userTenantId}")
    public ResponseEntity<EvaluationResultDto> getEvaluationResults(@PathVariable Integer userTenantId) {
        EvaluationResultDto dto = formService.getEvaluationResultForUser(userTenantId);
        return ResponseEntity.ok(dto);
    }


}
