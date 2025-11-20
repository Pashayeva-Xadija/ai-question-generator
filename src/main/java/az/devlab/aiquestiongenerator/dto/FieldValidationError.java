package az.devlab.aiquestiongenerator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldValidationError {

    private String field;

    private String message;
}
