package az.devlab.aiquestiongenerator.util;

public final class Constants {

    private Constants() {
    }
    public static final int DEFAULT_MAX_QUESTIONS_PER_REQUEST = 20;

    public static final String TOKEN_PREFIX_BEARER = "Bearer ";

    public static final int DEFAULT_MAX_TOKENS_FOR_AI = 512;
    public static final double DEFAULT_TEMPERATURE_FOR_AI = 0.7;

    public static final String SYSTEM_USERNAME = "SYSTEM_AI";

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;
}
