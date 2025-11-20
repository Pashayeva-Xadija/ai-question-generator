package az.devlab.aiquestiongenerator.controller;

import az.devlab.aiquestiongenerator.dto.QuestionApprovalRequest;
import az.devlab.aiquestiongenerator.dto.QuestionResponse;
import az.devlab.aiquestiongenerator.security.SecurityUtils;
import az.devlab.aiquestiongenerator.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionModerationController {

    private final QuestionService questionService;

    @PostMapping("/moderate")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public QuestionResponse moderateQuestion(@Valid @RequestBody QuestionApprovalRequest request) {
        Long moderatorId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return questionService.approveOrRejectQuestion(request, moderatorId);
    }
}
