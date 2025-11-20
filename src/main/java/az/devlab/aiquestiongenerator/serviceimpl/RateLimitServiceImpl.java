package az.devlab.aiquestiongenerator.serviceimpl;


import az.devlab.aiquestiongenerator.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final Map<String, Counter> inMemoryCounters = new ConcurrentHashMap<>();

    @Override
    public boolean tryConsume(String key, int maxRequests, int windowSeconds) {

        if (maxRequests <= 0) {
            return true;
        }

        if (redisTemplate != null) {
            return tryConsumeWithRedis(key, maxRequests, windowSeconds);
        } else {
            return tryConsumeInMemory(key, maxRequests, windowSeconds);
        }
    }


    private boolean tryConsumeWithRedis(String key, int maxRequests, int windowSeconds) {
        try {
            Long current = redisTemplate.opsForValue().increment(key);

            if (current != null && current == 1L) {
                // İlk request → TTL təyin olunur
                redisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
            }

            return current != null && current <= maxRequests;

        } catch (Exception e) {
            log.error("Redis rate limiting failed. Falling back to in-memory.", e);
            return tryConsumeInMemory(key, maxRequests, windowSeconds);
        }
    }

    private boolean tryConsumeInMemory(String key, int maxRequests, int windowSeconds) {
        long now = System.currentTimeMillis();
        long windowMillis = windowSeconds * 1000L;

        Counter counter = inMemoryCounters.compute(key, (k, existing) -> {
            if (existing == null || now - existing.windowStart > windowMillis) {
                return new Counter(1, now);
            } else {
                return new Counter(existing.count + 1, existing.windowStart);
            }
        });

        return counter.count <= maxRequests;
    }

    private record Counter(long count, long windowStart) {}
}

