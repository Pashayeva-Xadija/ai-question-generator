package az.devlab.aiquestiongenerator.service;

import az.devlab.aiquestiongenerator.dto.QuizCreateRequest;
import az.devlab.aiquestiongenerator.dto.QuizResponse;
import org.springframework.data.domain.Page;

public interface QuizService {

    QuizResponse createQuiz(QuizCreateRequest request, Long creatorId);

    QuizResponse getQuizById(Long id);

    Page<QuizResponse> getQuizzesForCreator(Long creatorId, int page, int size);
}
