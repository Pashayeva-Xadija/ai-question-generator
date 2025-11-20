package az.devlab.aiquestiongenerator.util;
public final class ApiErrorCodes {

    private ApiErrorCodes() {
    }

    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String QUESTION_LIMIT_EXCEEDED = "QUESTION_LIMIT_EXCEEDED";
    public static final String AI_SERVICE_UNAVAILABLE = "AI_SERVICE_UNAVAILABLE";
    public static final String AI_RESPONSE_MALFORMED = "AI_RESPONSE_MALFORMED";
    public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String RATE_LIMIT_EXCEEDED = "RATE_LIMIT_EXCEEDED";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
}
