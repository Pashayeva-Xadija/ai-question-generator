package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.dto.PagedResponse;
import az.devlab.aiquestiongenerator.dto.QuestionApprovalRequest;
import az.devlab.aiquestiongenerator.dto.QuestionFilterRequest;
import az.devlab.aiquestiongenerator.dto.QuestionResponse;
import az.devlab.aiquestiongenerator.enums.QuestionStatus;
import az.devlab.aiquestiongenerator.exception.BadRequestException;
import az.devlab.aiquestiongenerator.exception.NotFoundException;
import az.devlab.aiquestiongenerator.mapper.QuestionMapper;
import az.devlab.aiquestiongenerator.model.Question;
import az.devlab.aiquestiongenerator.model.User;
import az.devlab.aiquestiongenerator.repository.QuestionRepository;
import az.devlab.aiquestiongenerator.repository.UserRepository;
import az.devlab.aiquestiongenerator.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    @Override
    @Transactional(readOnly = true)
    public Question getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<QuestionResponse> searchQuestions(QuestionFilterRequest filterRequest) {
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), Sort.by("createdAt").descending());

        Page<Question> page;

        if (filterRequest.getTopic() != null &&
                filterRequest.getStatus() != null &&
                filterRequest.getDifficulty() != null &&
                filterRequest.getType() != null) {
            page = questionRepository.findByTopicIgnoreCaseAndStatusAndDifficultyAndType(
                    filterRequest.getTopic(),
                    filterRequest.getStatus(),
                    filterRequest.getDifficulty(),
                    filterRequest.getType(),
                    pageable
            );
        } else if (filterRequest.getTopic() != null && filterRequest.getStatus() != null) {
            page = questionRepository.findByTopicIgnoreCaseAndStatus(
                    filterRequest.getTopic(),
                    filterRequest.getStatus(),
                    pageable
            );
        } else if (filterRequest.getTopic() != null) {
            page = questionRepository.findByTopicIgnoreCase(
                    filterRequest.getTopic(),
                    pageable
            );
        } else if (filterRequest.getStatus() != null) {
            page = questionRepository.findByStatus(
                    filterRequest.getStatus(),
                    pageable
            );
        } else {
            page = questionRepository.findAll(pageable);
        }

        return page.map(questionMapper::toResponse);
    }

    @Override
    public QuestionResponse approveOrRejectQuestion(QuestionApprovalRequest request, Long moderatorId) {
        Question question = getById(request.getQuestionId());

        if (request.getStatus() == QuestionStatus.PENDING) {
            throw new BadRequestException("Cannot set status back to PENDING");
        }

        User moderator = userRepository.findById(moderatorId)
                .orElseThrow(() -> new NotFoundException("Moderator user not found with id: " + moderatorId));

        question.setStatus(request.getStatus());
        question.setApprovedBy(moderator);
        question.setApprovedAt(Instant.now());
        question.setModeratorComment(request.getComment());

        Question saved = questionRepository.save(question);
        return questionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse getQuestionResponseById(Long id) {
        Question question = getById(id);
        return questionMapper.toResponse(question);
    }

}

