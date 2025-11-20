package az.devlab.aiquestiongenerator.serviceimpl;

import az.devlab.aiquestiongenerator.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final Map<String, Long> inMemoryBlacklist = new ConcurrentHashMap<>();

    @Override
    public void blacklistToken(String token, long expiresInSeconds) {
        if (redisTemplate != null) {
            String key = buildKey(token);
            redisTemplate.opsForValue().set(key, Boolean.TRUE, Duration.ofSeconds(expiresInSeconds));
        } else {
            long expiryTime = System.currentTimeMillis() + expiresInSeconds * 1000L;
            inMemoryBlacklist.put(token, expiryTime);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        if (redisTemplate != null) {
            String key = buildKey(token);
            Boolean exists = redisTemplate.hasKey(key);
            return exists != null && exists;
        } else {
            Long expiry = inMemoryBlacklist.get(token);
            if (expiry == null) {
                return false;
            }
            if (System.currentTimeMillis() > expiry) {
                inMemoryBlacklist.remove(token);
                return false;
            }
            return true;
        }
    }

    private String buildKey(String token) {
        return "blacklist:token:" + token;
    }
}

