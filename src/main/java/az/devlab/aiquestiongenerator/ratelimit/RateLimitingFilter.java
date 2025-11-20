package az.devlab.aiquestiongenerator.ratelimit;

import az.devlab.aiquestiongenerator.config.RateLimitingConfig;
import az.devlab.aiquestiongenerator.dto.ApiErrorResponse;
import az.devlab.aiquestiongenerator.dto.FieldValidationError;
import az.devlab.aiquestiongenerator.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingConfig rateLimitingConfig;
    private final RateLimitKeyResolver keyResolver;
    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!rateLimitingConfig.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        if (path.startsWith("/actuator")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        int globalLimit = rateLimitingConfig.getGlobalRequestsPerMinute();
        int perUserLimit = rateLimitingConfig.getPerUserRequestsPerMinute();
        int windowSeconds = rateLimitingConfig.getWindowSeconds();

        String globalKey = keyResolver.resolveGlobalKey(request);
        String userKey = keyResolver.resolveUserKey(request);

        boolean globalAllowed = rateLimitService.tryConsume(globalKey, globalLimit, windowSeconds);
        boolean userAllowed = rateLimitService.tryConsume(userKey, perUserLimit, windowSeconds);

        if (!globalAllowed || !userAllowed) {
            log.warn("Rate limit exceeded for request: {} {}", request.getMethod(), path);
            writeRateLimitError(response, request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeRateLimitError(HttpServletResponse response, String path) throws IOException {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.TOO_MANY_REQUESTS.value())
                .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                .message("Rate limit exceeded. Please try again later.")
                .path(path)
                .fieldErrors(List.of(
                        FieldValidationError.builder()
                                .field("rateLimit")
                                .message("Too many requests in the given time window.")
                                .build()
                ))
                .build();

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        new com.fasterxml.jackson.databind.ObjectMapper()
                .writeValue(response.getOutputStream(), error);
    }
}
