package az.devlab.aiquestiongenerator.service;

public interface RateLimitService {

    boolean tryConsume(String key, int maxRequests, int windowSeconds);
}

