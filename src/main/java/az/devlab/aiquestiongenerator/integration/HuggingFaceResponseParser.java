package az.devlab.aiquestiongenerator.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HuggingFaceResponseParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HuggingFaceResponsePayload parse(String rawJson) {
        try {
            return objectMapper.readValue(rawJson, HuggingFaceResponsePayload.class);
        } catch (Exception e) {
            log.error("Failed to parse HF single-object response", e);
            throw new RuntimeException("Failed to parse HF response", e);
        }
    }

    public HuggingFaceResponsePayload[] parseArray(String rawJson) {
        try {
            return objectMapper.readValue(rawJson, HuggingFaceResponsePayload[].class);
        } catch (Exception e) {
            log.error("Failed to parse HF array response: {}", rawJson, e);
            throw new RuntimeException("Failed to parse HF array response", e);
        }
    }
}


