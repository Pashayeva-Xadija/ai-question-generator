package az.devlab.aiquestiongenerator.serviceimpl;


import az.devlab.aiquestiongenerator.dto.QuizCreateRequest;
import az.devlab.aiquestiongenerator.dto.QuizResponse;
import az.devlab.aiquestiongenerator.exception.NotFoundException;
import az.devlab.aiquestiongenerator.mapper.QuestionMapper;
import az.devlab.aiquestiongenerator.mapper.QuizMapper;
import az.devlab.aiquestiongenerator.model.Question;
import az.devlab.aiquestiongenerator.model.Quiz;
import az.devlab.aiquestiongenerator.model.QuizQuestion;
import az.devlab.aiquestiongenerator.model.User;
import az.devlab.aiquestiongenerator.repository.QuestionRepository;
import az.devlab.aiquestiongenerator.repository.QuizQuestionRepository;
import az.devlab.aiquestiongenerator.repository.QuizRepository;
import az.devlab.aiquestiongenerator.repository.UserRepository;
import az.devlab.aiquestiongenerator.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuizMapper quizMapper;

    @Override
    public QuizResponse createQuiz(QuizCreateRequest request, Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + creatorId));

        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .difficulty(request.getDifficulty())
                .createdBy(creator)
                .build();

        Quiz savedQuiz = quizRepository.save(quiz);

        int index = 1;
        for (Long qid : request.getQuestionIds()) {
            Question question = questionRepository.findById(qid)
                    .orElseThrow(() -> new NotFoundException("Question not found with id: " + qid));
            QuizQuestion qq = QuizQuestion.builder()
                    .quiz(savedQuiz)
                    .question(question)
                    .orderIndex(index++)
                    .points(1)
                    .build();
            quizQuestionRepository.save(qq);
        }

        return quizMapper.toResponse(savedQuiz);
    }

    @Override
    @Transactional(readOnly = true)
    public QuizResponse getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz not found with id: " + id));
        return quizMapper.toResponse(quiz);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<QuizResponse> getQuizzesForCreator(Long creatorId, int page, int size) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + creatorId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Quiz> quizPage = quizRepository.findByCreatedBy(creator, pageable);

        return quizPage.map(quizMapper::toResponse);
    }
}
