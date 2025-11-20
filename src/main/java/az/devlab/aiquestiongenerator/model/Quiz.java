package az.devlab.aiquestiongenerator.model;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 20)
    private DifficultyLevel difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id",
            foreignKey = @ForeignKey(name = "fk_quiz_created_by"))
    private User createdBy;

    @OneToMany(
            mappedBy = "quiz",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<QuizQuestion> quizQuestions = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public void addQuizQuestion(QuizQuestion qq) {
        quizQuestions.add(qq);
        qq.setQuiz(this);
    }

    public void removeQuizQuestion(QuizQuestion qq) {
        quizQuestions.remove(qq);
        qq.setQuiz(null);
    }
}
