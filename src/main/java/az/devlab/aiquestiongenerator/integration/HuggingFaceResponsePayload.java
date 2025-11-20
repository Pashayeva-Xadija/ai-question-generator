package az.devlab.aiquestiongenerator.integration;

import lombok.Data;

import java.util.List;

@Data
public class HuggingFaceResponsePayload {

    private String model;
    private String questionText;
    private String topic;
    private String explanation;
    private String alternativeText;

    private String difficulty;
    private String type;
    private List<HuggingFaceResponseOption> options;
}
