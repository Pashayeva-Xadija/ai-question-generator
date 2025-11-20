package az.devlab.aiquestiongenerator.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "quiz_questions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_quiz_question_quiz_question",
                        columnNames = {"quiz_id", "question_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_quiz_question_quiz"))
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_quiz_question_question"))
    private Question question;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "points")
    private Integer points;
}
