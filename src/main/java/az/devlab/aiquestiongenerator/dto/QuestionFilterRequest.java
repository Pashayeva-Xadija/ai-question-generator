package az.devlab.aiquestiongenerator.dto;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.enums.QuestionStatus;
import az.devlab.aiquestiongenerator.enums.QuestionType;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class QuestionFilterRequest {

    private String topic;

    private DifficultyLevel difficulty;

    private QuestionStatus status;

    private QuestionType type;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 20;
}

