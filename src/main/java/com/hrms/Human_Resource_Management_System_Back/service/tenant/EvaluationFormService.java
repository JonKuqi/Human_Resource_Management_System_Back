package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.model.dto.AnswerDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.EvaluationResultDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.QuestionAverageDto;
import com.hrms.Human_Resource_Management_System_Back.model.dto.SubmitEvaluationFormRequest;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.*;
import com.hrms.Human_Resource_Management_System_Back.repository.tenant.*;
import com.hrms.Human_Resource_Management_System_Back.service.BaseUserSpecificService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationFormService extends BaseUserSpecificService<EvaluationForm, Integer> {
    private final EvaluationFormRepository repo;
    private final EvaluationQuestionRepository questionRepository;
    private final EvaluationAnswerRepository answerRepository;
    private final EvaluationFormRepository formRepository;
    private final UserTenantRepository userTenantRepository;
    private final EvaluationTemplateRepository templateRepository;

    @Override
    protected EvaluationFormRepository getRepository() {
        return repo;
    }

    public Optional<EvaluationForm> findFormForEvaluator(Integer formId, Integer evaluatorId) {
        return repo.findByIdAndEvaluatorUserTenantId(formId, evaluatorId);
    }

    public List<EvaluationQuestion> getQuestionsForForm(EvaluationForm form) {
        return questionRepository.findByTemplateId(form.getTemplate().getId());
    }

    public void submitForm(Integer formId, Integer evaluatorId, SubmitEvaluationFormRequest rq) {
        EvaluationForm form = getRepository().findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        if (!form.getEvaluator().getUserTenantId().equals(evaluatorId)) {
            throw new RuntimeException("Unauthorized to submit this form.");
        }

        if (!"PENDING".equals(form.getStatus())) {
            throw new RuntimeException("Form already submitted.");
        }

        List<EvaluationQuestion> questions = questionRepository
                .findByTemplateId(form.getTemplate().getId());

        Map<Integer, EvaluationQuestion> questionMap = questions.stream()
                .collect(Collectors.toMap(EvaluationQuestion::getId, q -> q));

        List<EvaluationAnswer> answers = new ArrayList<>();

        for (AnswerDto a : rq.getAnswers()) {
            EvaluationQuestion q = questionMap.get(a.getQuestionId());

            if (q == null) continue;

            EvaluationAnswer answer = EvaluationAnswer.builder()
                    .form(form)
                    .question(q)
                    .rating(a.getRating())
                    .textResponse(a.getTextResponse())
                    .build();

            answers.add(answer);
        }

        answerRepository.saveAll(answers);

        form.setStatus("COMPLETED");
        form.setSubmittedAt(LocalDateTime.now());
        getRepository().save(form);
    }

    public EvaluationResultDto getEvaluationResultForUser(Integer userTenantId) {

        // Gjej të gjitha format e dorëzuara për këtë user
        List<EvaluationForm> forms = formRepository.findByForUser_UserTenantIdAndStatus(userTenantId, "COMPLETED");

        if (forms.isEmpty()) {
            return EvaluationResultDto.builder()
                    .evaluatedUserId(userTenantId)
                    .completedForms(0)
                    .averageRating(0.0)
                    .questionAverages(List.of())
                    .comments(List.of())
                    .suggestedRaisePercentage(0)
                    .build();
        }

        List<EvaluationAnswer> allAnswers = answerRepository.findByFormIn(forms);

        // Pyetje të veçanta dhe mesataret
        Map<String, List<Integer>> questionRatings = new HashMap<>();
        List<String> comments = new ArrayList<>();

        for (EvaluationAnswer answer : allAnswers) {
            String questionText = answer.getQuestion().getQuestionText();

            if (answer.getRating() != null) {
                questionRatings.computeIfAbsent(questionText, k -> new ArrayList<>())
                        .add(answer.getRating());
            }

            if (answer.getTextResponse() != null && !answer.getTextResponse().isBlank()) {
                comments.add(answer.getTextResponse());
            }
        }

        // Llogarit mesataren totale
        List<Integer> allRatings = questionRatings.values()
                .stream()
                .flatMap(Collection::stream)
                .toList();

        double overallAverage = allRatings.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        List<QuestionAverageDto> perQuestion = questionRatings.entrySet()
                .stream()
                .map(e -> new QuestionAverageDto(
                        e.getKey(),
                        e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0.0)
                ))
                .toList();

        // Logjikë për rritje page në bazë të mesatares
        int raisePercentage = 0;
        if (overallAverage >= 4.5) raisePercentage = 10;
        else if (overallAverage >= 4.0) raisePercentage = 7;
        else if (overallAverage >= 3.5) raisePercentage = 5;

        return EvaluationResultDto.builder()
                .evaluatedUserId(userTenantId)
                .completedForms(forms.size())
                .averageRating(overallAverage)
                .questionAverages(perQuestion)
                .comments(comments)
                .suggestedRaisePercentage(raisePercentage)
                .build();
    }
    public void initiateEvaluation(Integer templateId, UserTenant target, UserTenant initiatedBy) {
        List<UserTenant> allUsers = userTenantRepository.findAll();

        for (UserTenant evaluator : allUsers) {
            if (evaluator.getUserTenantId().equals(target.getUserTenantId())) continue;

            EvaluationForm form = EvaluationForm.builder()
                    .template(templateRepository.getReferenceById(templateId))
                    .forUser(target)
                    .evaluator(evaluator)
                    .status("PENDING")
                    .createdAt(LocalDateTime.now())
                    .build();

            formRepository.save(form);
        }
    }


}