package az.devlab.aiquestiongenerator.service;

import az.devlab.aiquestiongenerator.dto.QuestionGenerationRequest;
import az.devlab.aiquestiongenerator.integration.HuggingFaceResponsePayload;
import java.util.List;

public interface AiQuestionClient {
    List<HuggingFaceResponsePayload> generateRawQuestions(QuestionGenerationRequest request, int normalizedCount);
}
