package az.devlab.aiquestiongenerator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionOptionResponse {

    private Long id;

    private String text;

    private boolean correct;
}
