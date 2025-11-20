package az.devlab.aiquestiongenerator.util;

import az.devlab.aiquestiongenerator.exception.ValidationException;
import az.devlab.aiquestiongenerator.model.QuestionOption;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class QuestionGenerationUtils {

    private QuestionGenerationUtils() {
    }


    public static int normalizeRequestedCount(int requested, int maxAllowed) {
        if (requested <= 0) {
            throw new ValidationException("Question count must be greater than zero.");
        }
        if (maxAllowed <= 0) {
            return requested;
        }
        return Math.min(requested, maxAllowed);
    }

    public static void shuffleOptions(List<QuestionOption> options) {
        if (options == null || options.size() <= 1) {
            return;
        }
        Collections.shuffle(options);
    }

    public static void ensureSingleCorrectOption(List<QuestionOption> options) {
        if (options == null || options.isEmpty()) {
            throw new ValidationException("Question must have at least one option.");
        }

        long correctCount = options.stream()
                .filter(Objects::nonNull)
                .filter(QuestionOption::isCorrect)
                .count();

        if (correctCount == 0) {
            throw new ValidationException("Question must have exactly one correct option, but none was found.");
        }
        if (correctCount > 1) {
            throw new ValidationException("Question must have exactly one correct option, but multiple were marked as correct.");
        }
    }
}


