package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.dto.QuestionGenerationRequest;
import az.devlab.aiquestiongenerator.exception.AiServiceException;
import az.devlab.aiquestiongenerator.integration.HuggingFaceRequestPayload;
import az.devlab.aiquestiongenerator.integration.HuggingFaceResponseParser;
import az.devlab.aiquestiongenerator.integration.HuggingFaceResponsePayload;
import az.devlab.aiquestiongenerator.service.AiQuestionClient;
import az.devlab.aiquestiongenerator.service.PromptBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiQuestionClientImpl implements AiQuestionClient {

    @Qualifier("huggingFaceWebClient")
    private final WebClient webClient;
    private final PromptBuilderService promptBuilderService;
    private final HuggingFaceResponseParser responseParser;

    @Override
    public List<HuggingFaceResponsePayload> generateRawQuestions(QuestionGenerationRequest request, int normalizedCount) {
        String prompt = promptBuilderService.buildQuestionGenerationPrompt(request, normalizedCount);

        HuggingFaceRequestPayload payload = HuggingFaceRequestPayload.builder()
                .prompt(prompt)
                .maxTokens(1024)
                .temperature(0.7)
                .build();

        try {
            String rawResponse = webClient
                    .post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(60));

            if (rawResponse == null || rawResponse.isBlank()) {
                throw new AiServiceException("Empty response from HuggingFace API");
            }

            HuggingFaceResponsePayload[] arr = responseParser.parseArray(rawResponse);
            return Arrays.asList(arr);

        } catch (Exception e) {
            log.error("Error calling HuggingFace API", e);
            throw new AiServiceException("Failed to call HuggingFace API", e);
        }
    }
}
