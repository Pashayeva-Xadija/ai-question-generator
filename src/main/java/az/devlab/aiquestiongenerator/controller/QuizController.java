package az.devlab.aiquestiongenerator.controller;

import az.devlab.aiquestiongenerator.dto.PagedResponse;
import az.devlab.aiquestiongenerator.dto.QuizCreateRequest;
import az.devlab.aiquestiongenerator.dto.QuizResponse;
import az.devlab.aiquestiongenerator.security.SecurityUtils;
import az.devlab.aiquestiongenerator.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public QuizResponse createQuiz(@Valid @RequestBody QuizCreateRequest request) {
        Long creatorId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return quizService.createQuiz(request, creatorId);
    }

    @GetMapping("/{id}")
    public QuizResponse getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id);
    }


    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public PagedResponse<QuizResponse> getMyQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Long creatorId = SecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Page<QuizResponse> quizPage = quizService.getQuizzesForCreator(creatorId, page, size);

        return PagedResponse.<QuizResponse>builder()
                .content(quizPage.getContent())
                .page(quizPage.getNumber())
                .size(quizPage.getSize())
                .totalElements(quizPage.getTotalElements())
                .totalPages(quizPage.getTotalPages())
                .last(quizPage.isLast())
                .build();
    }
}
