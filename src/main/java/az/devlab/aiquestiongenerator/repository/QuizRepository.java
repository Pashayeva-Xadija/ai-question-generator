package az.devlab.aiquestiongenerator.repository;

import az.devlab.aiquestiongenerator.model.Quiz;
import az.devlab.aiquestiongenerator.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Page<Quiz> findByCreatedBy(User createdBy, Pageable pageable);
}

