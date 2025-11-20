package az.devlab.aiquestiongenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationProperties {


    private HuggingFaceProperties huggingFace = new HuggingFaceProperties();

    @Data
    public static class HuggingFaceProperties {

        private String baseUrl = "https://api-inference.huggingface.co";

        private String model;

        private String apiKey;

        private Duration timeout = Duration.ofSeconds(30);
    }
}
