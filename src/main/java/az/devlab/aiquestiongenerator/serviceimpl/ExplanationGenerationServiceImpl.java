package az.devlab.aiquestiongenerator.serviceimpl;


import az.devlab.aiquestiongenerator.service.ExplanationGenerationService;
import az.devlab.aiquestiongenerator.service.PromptBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExplanationGenerationServiceImpl implements ExplanationGenerationService {

    @Qualifier("huggingFaceWebClient")
    private final WebClient webClient;

    private final PromptBuilderService promptBuilderService;

    @Override
    public String generateExplanation(String questionText, String correctAnswer) {
        String prompt = promptBuilderService.buildExplanationPrompt(questionText, correctAnswer);

        try {
            return webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"inputs\": " + "\"" + prompt.replace("\"", "\\\"") + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Failed to generate explanation using AI", e);
            return null;
        }
    }
}

