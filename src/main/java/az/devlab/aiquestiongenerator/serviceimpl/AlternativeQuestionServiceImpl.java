package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.service.AlternativeQuestionService;
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
public class AlternativeQuestionServiceImpl implements AlternativeQuestionService {

    @Qualifier("huggingFaceWebClient")
    private final WebClient webClient;

    private final PromptBuilderService promptBuilderService;

    @Override
    public String generateAlternativeQuestion(String originalQuestionText) {
        String prompt = promptBuilderService.buildAlternativeQuestionPrompt(originalQuestionText);

        try {
            return webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"inputs\": " + "\"" + prompt.replace("\"", "\\\"") + "\"}")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(30));
        } catch (Exception e) {
            log.error("Failed to generate alternative question using AI", e);
            return null;
        }
    }
}
