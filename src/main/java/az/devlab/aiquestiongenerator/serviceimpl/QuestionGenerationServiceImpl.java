package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.config.QuestionGenerationProperties;
import az.devlab.aiquestiongenerator.dto.QuestionGenerationRequest;
import az.devlab.aiquestiongenerator.dto.QuestionGenerationResultResponse;
import az.devlab.aiquestiongenerator.dto.QuestionResponse;
import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.exception.ValidationException;
import az.devlab.aiquestiongenerator.integration.HuggingFaceResponsePayload;
import az.devlab.aiquestiongenerator.mapper.AiQuestionMapper;
import az.devlab.aiquestiongenerator.mapper.QuestionMapper;
import az.devlab.aiquestiongenerator.model.Question;
import az.devlab.aiquestiongenerator.service.AiQuestionClient;
import az.devlab.aiquestiongenerator.service.QuestionGenerationService;
import az.devlab.aiquestiongenerator.service.QuestionService;
import az.devlab.aiquestiongenerator.security.SecurityUtils;
import az.devlab.aiquestiongenerator.util.QuestionGenerationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionGenerationServiceImpl implements QuestionGenerationService {

    private final QuestionGenerationProperties generationProperties;
    private final AiQuestionClient aiQuestionClient;
    private final AiQuestionMapper aiQuestionMapper;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionGenerationResultResponse generateQuestions(QuestionGenerationRequest request) {
        int normalizedCount = QuestionGenerationUtils.normalizeRequestedCount(
                request.getQuestionCount(),
                generationProperties.getMaxQuestionsPerRequest()
        );

        List<HuggingFaceResponsePayload> payloads =
                aiQuestionClient.generateRawQuestions(request, normalizedCount);

        Long currentUserId = SecurityUtils.getCurrentUserId().orElse(null);

        DifficultyLevel difficulty = request.getDifficulty() != null
                ? request.getDifficulty()
                : DifficultyLevel.valueOf(generationProperties.getDefaultDifficulty());

        List<QuestionResponse> questionResponses = payloads.stream()
                .map(p -> {
                    Question q = aiQuestionMapper.toEntity(p);
                    q.setTopic(request.getTopic());
                    q.setDifficulty(difficulty);

                    QuestionGenerationUtils.ensureSingleCorrectOption(q.getOptions());
                    QuestionGenerationUtils.shuffleOptions(q.getOptions());

                    Question saved = questionService.save(q);
                    return questionMapper.toResponse(saved);
                })
                .toList();

        String warning = null;
        if (questionResponses.size() != normalizedCount) {
            warning = "Requested " + normalizedCount + " questions, but AI returned "
                    + questionResponses.size() + ".";
        }

        return QuestionGenerationResultResponse.builder()
                .topic(request.getTopic())
                .difficulty(difficulty)
                .type(request.getType())
                .generatedCount(questionResponses.size())
                .questions(questionResponses)
                .warning(warning)
                .build();
    }
}
