package az.devlab.aiquestiongenerator.service;

import az.devlab.aiquestiongenerator.dto.QuestionGenerationRequest;

public interface PromptBuilderService {

    String buildQuestionGenerationPrompt(QuestionGenerationRequest request, int normalizedCount);

    String buildExplanationPrompt(String questionText, String correctAnswer);

    String buildAlternativeQuestionPrompt(String originalQuestionText);
}
