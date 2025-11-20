package az.devlab.aiquestiongenerator.repository;

import az.devlab.aiquestiongenerator.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findByQuizIdOrderByOrderIndexAsc(Long quizId);

    void deleteByQuizId(Long quizId);
}
