package az.devlab.aiquestiongenerator.dto;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.enums.QuestionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionGenerationRequest {

    @NotBlank
    private String topic;

    @Min(1)
    @Max(100)
    private int questionCount;

    private DifficultyLevel difficulty;

    private QuestionType type;

    private boolean includeExplanations = true;

    private boolean includeAlternatives = true;
}
