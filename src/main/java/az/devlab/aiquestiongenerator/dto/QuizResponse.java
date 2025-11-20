package az.devlab.aiquestiongenerator.dto;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class QuizResponse {

    private Long id;

    private String title;

    private String description;

    private DifficultyLevel difficulty;

    private Long createdById;

    private Instant createdAt;

    private Instant updatedAt;

    private List<QuestionResponse> questions;
}
