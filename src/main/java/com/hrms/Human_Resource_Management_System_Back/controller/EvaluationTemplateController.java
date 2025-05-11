package com.hrms.Human_Resource_Management_System_Back.controller;

import com.hrms.Human_Resource_Management_System_Back.model.dto.InitiateEvaluationRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationForm;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.EvaluationTemplate;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserTenant;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserRoleRepository;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.UserTenantRepository;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.EvaluationFormService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.EvaluationTemplateService;
import com.hrms.Human_Resource_Management_System_Back.model.dto.CreateEvaluationTemplateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tenant/evaluation-templates")
@RequiredArgsConstructor
public class EvaluationTemplateController extends BaseUserSpecificController<EvaluationTemplate, Integer> {

    private final EvaluationTemplateService service;
    private final UserTenantRepository userTenantRepository;
    private final EvaluationFormService evaluationFormService;
    private final UserRoleRepository userRoleRepository;

    @Override
    protected EvaluationTemplateService getServiceSpecific() {
        return service;
    }

    @PostMapping
    public ResponseEntity<?> createEvaluationTemplate(@RequestBody CreateEvaluationTemplateRequest rq) {
        Integer userTenantId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserTenant creator = userTenantRepository.findById(userTenantId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EvaluationTemplate created = service.createTemplate(rq, creator);
        return ResponseEntity.ok(Map.of("templateId", created.getId()));
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateEvaluation(@RequestBody InitiateEvaluationRequest rq,
                                                HttpServletRequest request) {

        Integer evaluatorId = (Integer) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        // Marrim user-in që po inicion vlerësimin (mund ta ruajmë për log ose kontroll)
        UserTenant evaluator = userTenantRepository.findById(evaluatorId)
                .orElseThrow(() -> new RuntimeException("Evaluator not found"));

        // Marrim target user-in që do të vlerësohet
        UserTenant target = userTenantRepository.findById(rq.getTargetUserTenantId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        // Marrim target rolet që ky përdorues ka të drejtë të vlerësojë
        List<String> allowedTargetRoles = (List<String>) request.getAttribute("target_roles");

        if (allowedTargetRoles == null || allowedTargetRoles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You have no target role permissions."));
        }

        // Marrim rolet reale të user-it target nga user_role
        List<String> targetUserRoles = userRoleRepository.findRoleNamesByUserTenantId(target.getUserTenantId());

        // Verifikojmë nëse ndonjë rol i target-it është në listën e lejuar
        boolean canEvaluate = targetUserRoles.stream()
                .anyMatch(allowedTargetRoles::contains);

        if (!canEvaluate) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You are not allowed to evaluate users with roles: " + targetUserRoles));
        }

        // Kalo te servisi për të shpërndarë formën te vlerësuesit
        evaluationFormService.initiateEvaluation(rq.getTemplateId(), target, evaluator);

        return ResponseEntity.ok(Map.of("message", "Evaluation initiated successfully"));
    }


}