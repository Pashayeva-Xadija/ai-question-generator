package az.devlab.aiquestiongenerator.service;

import az.devlab.aiquestiongenerator.dto.QuestionGenerationRequest;
import az.devlab.aiquestiongenerator.dto.QuestionGenerationResultResponse;

public interface QuestionGenerationService {

    QuestionGenerationResultResponse generateQuestions(QuestionGenerationRequest request);
}
