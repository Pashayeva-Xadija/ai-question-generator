package az.devlab.aiquestiongenerator.dto;

import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuizCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    private DifficultyLevel difficulty;

    @NotEmpty
    private List<Long> questionIds;
}
