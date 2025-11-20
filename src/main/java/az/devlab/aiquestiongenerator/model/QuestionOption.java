package az.devlab.aiquestiongenerator.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "question_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_question_option_question"))
    private Question question;
}
