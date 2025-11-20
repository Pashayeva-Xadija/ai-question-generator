package az.devlab.aiquestiongenerator.model;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.enums.QuestionStatus;
import az.devlab.aiquestiongenerator.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @Column(nullable = false, length = 255)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DifficultyLevel difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuestionStatus status;

    @Column(columnDefinition = "text")
    private String explanation;

    @Column(name = "alternative_text", columnDefinition = "text")
    private String alternativeText;

    @Column(name = "ai_model", length = 255)
    private String aiModel;

    @Column(name = "ai_request_id", length = 255)
    private String aiRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id",
            foreignKey = @ForeignKey(name = "fk_question_created_by"))
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id",
            foreignKey = @ForeignKey(name = "fk_question_approved_by"))
    private User approvedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<QuestionOption> options = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        if (difficulty == null) {
            difficulty = DifficultyLevel.MEDIUM;
        }
        if (status == null) {
            status = QuestionStatus.PENDING;
        }
        if (type == null) {
            type = QuestionType.MULTIPLE_CHOICE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public void addOption(QuestionOption option) {
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(QuestionOption option) {
        options.remove(option);
        option.setQuestion(null);
    }

    @Column(name = "moderator_comment", columnDefinition = "text")
    private String moderatorComment;

}
