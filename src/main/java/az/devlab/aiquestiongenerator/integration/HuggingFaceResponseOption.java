package az.devlab.aiquestiongenerator.integration;


import lombok.Data;

@Data
public class HuggingFaceResponseOption {
    private String text;
    private boolean correct;
}
