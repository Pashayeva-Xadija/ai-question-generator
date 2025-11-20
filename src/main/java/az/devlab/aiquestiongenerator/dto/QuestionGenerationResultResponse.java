package az.devlab.aiquestiongenerator.dto;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionGenerationResultResponse {

    private String topic;

    private DifficultyLevel difficulty;

    private QuestionType type;

    private int generatedCount;

    private List<QuestionResponse> questions;

    private String warning;
}
