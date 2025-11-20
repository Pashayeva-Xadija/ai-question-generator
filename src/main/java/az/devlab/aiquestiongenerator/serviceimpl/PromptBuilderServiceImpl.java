package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.dto.QuestionGenerationRequest;
import az.devlab.aiquestiongenerator.enums.DifficultyLevel;
import az.devlab.aiquestiongenerator.enums.QuestionType;
import az.devlab.aiquestiongenerator.service.PromptBuilderService;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderServiceImpl implements PromptBuilderService {

    @Override
    public String buildQuestionGenerationPrompt(QuestionGenerationRequest request, int normalizedCount) {
        DifficultyLevel difficulty = request.getDifficulty() != null
                ? request.getDifficulty()
                : DifficultyLevel.MEDIUM;

        QuestionType type = request.getType() != null
                ? request.getType()
                : QuestionType.MULTIPLE_CHOICE;

        StringBuilder sb = new StringBuilder();

        sb.append("You are an AI that generates quiz questions for educators.\n");
        sb.append("Generate ")
                .append(normalizedCount)
                .append(" ")
                .append(type.name().toLowerCase().replace("_", " "))
                .append(" questions about the topic: '")
                .append(request.getTopic())
                .append("'.\n");
        sb.append("Difficulty level: ").append(difficulty.name()).append(".\n");
        sb.append("Return the result as a strict JSON array where each element has this structure:\n");
        sb.append("{\n");
        sb.append("  \"model\": \"string\",\n");
        sb.append("  \"questionText\": \"string\",\n");
        sb.append("  \"topic\": \"string\",\n");
        sb.append("  \"difficulty\": \"EASY|MEDIUM|HARD\",\n");
        sb.append("  \"type\": \"MULTIPLE_CHOICE\",\n");
        sb.append("  \"explanation\": \"string (short explanation for the correct answer)\",\n");
        sb.append("  \"alternativeText\": \"string (alternative phrasing of the question)\",\n");
        sb.append("  \"options\": [\n");
        sb.append("    {\"text\": \"string\", \"correct\": true|false}\n");
        sb.append("  ]\n");
        sb.append("}\n");
        sb.append("Make sure exactly ONE option has \"correct\": true for each question.\n");
        sb.append("Do not include any text before or after the JSON array. Only output valid JSON.\n");

        return sb.toString();
    }

    @Override
    public String buildExplanationPrompt(String questionText, String correctAnswer) {
        return "Provide a short, clear explanation for why the following answer is correct.\n" +
                "Question: " + questionText + "\n" +
                "Correct answer: " + correctAnswer + "\n" +
                "Return a single-paragraph explanation in plain text.";
    }

    @Override
    public String buildAlternativeQuestionPrompt(String originalQuestionText) {
        return "Rewrite the following question into an alternative phrasing with the same meaning:\n" +
                originalQuestionText + "\n" +
                "Return only the new question text.";
    }
}
