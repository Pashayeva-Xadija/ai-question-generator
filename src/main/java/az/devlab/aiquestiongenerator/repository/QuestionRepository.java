package az.devlab.aiquestiongenerator.repository;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.enums.QuestionStatus;
import az.devlab.aiquestiongenerator.enums.QuestionType;
import az.devlab.aiquestiongenerator.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findByTopicIgnoreCase(String topic, Pageable pageable);

    Page<Question> findByStatus(QuestionStatus status, Pageable pageable);

    Page<Question> findByTopicIgnoreCaseAndStatus(String topic,
                                                  QuestionStatus status,
                                                  Pageable pageable);

    Page<Question> findByTopicIgnoreCaseAndStatusAndDifficultyAndType(
            String topic,
            QuestionStatus status,
            DifficultyLevel difficulty,
            QuestionType type,
            Pageable pageable
    );
}
