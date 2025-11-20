package az.devlab.aiquestiongenerator.dto;

import az.devlab.aiquestiongenerator.enums.QuestionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionApprovalRequest {

    @NotNull
    private Long questionId;

    @NotNull
    private QuestionStatus status;

    private String comment;
}
