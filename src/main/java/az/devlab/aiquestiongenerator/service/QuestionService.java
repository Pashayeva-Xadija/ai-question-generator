package az.devlab.aiquestiongenerator.service;

import az.devlab.aiquestiongenerator.dto.*;
import az.devlab.aiquestiongenerator.model.Question;
import org.springframework.data.domain.Page;

public interface QuestionService {

    Question save(Question question);

    Question getById(Long id);

    Page<QuestionResponse> searchQuestions(QuestionFilterRequest filterRequest);

    QuestionResponse approveOrRejectQuestion(QuestionApprovalRequest request, Long moderatorId);

    QuestionResponse getQuestionResponseById(Long id);



}
