package az.devlab.aiquestiongenerator.integration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "huggingface")
public class HuggingFaceProperties {

    private String baseUrl;
    private String model;
    private String apiKey;
    private Duration timeout = Duration.ofSeconds(30);
}
