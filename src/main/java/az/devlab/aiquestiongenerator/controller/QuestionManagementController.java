package az.devlab.aiquestiongenerator.controller;

import az.devlab.aiquestiongenerator.dto.PagedResponse;
import az.devlab.aiquestiongenerator.dto.QuestionFilterRequest;
import az.devlab.aiquestiongenerator.dto.QuestionResponse;
import az.devlab.aiquestiongenerator.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionManagementController {

    private final QuestionService questionService;


    @GetMapping
    public PagedResponse<QuestionResponse> getQuestions(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        QuestionFilterRequest filter = new QuestionFilterRequest();
        filter.setTopic(topic);

        filter.setPage(page);
        filter.setSize(size);

        Page<QuestionResponse> questionPage = questionService.searchQuestions(filter);

        return PagedResponse.<QuestionResponse>builder()
                .content(questionPage.getContent())
                .page(questionPage.getNumber())
                .size(questionPage.getSize())
                .totalElements(questionPage.getTotalElements())
                .totalPages(questionPage.getTotalPages())
                .last(questionPage.isLast())
                .build();
    }

    @GetMapping("/{id}")
    public QuestionResponse getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionResponseById(id);
    }

}

