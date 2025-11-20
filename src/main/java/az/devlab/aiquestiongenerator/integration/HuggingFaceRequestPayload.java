package az.devlab.aiquestiongenerator.integration;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HuggingFaceRequestPayload {

    private String prompt;
    private int maxTokens;
    private double temperature;
}
