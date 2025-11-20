package az.devlab.aiquestiongenerator.repository;

import az.devlab.aiquestiongenerator.model.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByQuestionId(Long questionId);

    void deleteByQuestionId(Long questionId);
}
