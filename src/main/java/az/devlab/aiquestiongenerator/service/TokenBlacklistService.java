package az.devlab.aiquestiongenerator.service;

public interface TokenBlacklistService {

    void blacklistToken(String token, long expiresInSeconds);

    boolean isTokenBlacklisted(String token);
}

