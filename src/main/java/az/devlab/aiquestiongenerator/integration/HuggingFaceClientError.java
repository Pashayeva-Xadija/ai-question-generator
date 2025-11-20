package az.devlab.aiquestiongenerator.integration;

public class HuggingFaceClientError extends RuntimeException {

    public HuggingFaceClientError(String message) {
        super(message);
    }

    public HuggingFaceClientError(String message, Throwable cause) {
        super(message, cause);
    }
}
