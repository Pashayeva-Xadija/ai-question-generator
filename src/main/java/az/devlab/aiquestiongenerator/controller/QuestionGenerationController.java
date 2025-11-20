package az.devlab.aiquestiongenerator.controller;

import az.devlab.aiquestiongenerator.dto.QuestionGenerationRequest;
import az.devlab.aiquestiongenerator.dto.QuestionGenerationResultResponse;
import az.devlab.aiquestiongenerator.service.QuestionGenerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionGenerationController {

    private final QuestionGenerationService questionGenerationService;

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public QuestionGenerationResultResponse generateQuestions(
            @Valid @RequestBody QuestionGenerationRequest request
    ) {
        return questionGenerationService.generateQuestions(request);
    }
}

