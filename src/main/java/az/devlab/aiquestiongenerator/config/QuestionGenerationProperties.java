package az.devlab.aiquestiongenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "question.generation")
@Data
public class QuestionGenerationProperties {

    private int maxQuestionsPerRequest = 20;

    private String defaultDifficulty = "MEDIUM";

    private boolean allowMultipleTypes = true;

    private boolean enableExplanations = true;

    private boolean enableAlternativeQuestions = true;
}
