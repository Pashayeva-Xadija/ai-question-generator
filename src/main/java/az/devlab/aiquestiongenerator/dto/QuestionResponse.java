package az.devlab.aiquestiongenerator.dto;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.enums.QuestionStatus;
import az.devlab.aiquestiongenerator.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class QuestionResponse {

    private Long id;

    private String text;

    private String topic;

    private DifficultyLevel difficulty;

    private QuestionType type;

    private QuestionStatus status;

    private String explanation;

    private String alternativeText;

    private String aiModel;

    private Long createdById;

    private Long approvedById;

    private Instant createdAt;

    private Instant approvedAt;

    private List<QuestionOptionResponse> options;
}
