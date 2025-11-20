package az.devlab.aiquestiongenerator.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuizAssignRequest {

    @NotNull
    private Long quizId;

    @NotEmpty
    private List<Long> userIds;
}
