package az.devlab.aiquestiongenerator.ratelimit;

import az.devlab.aiquestiongenerator.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RateLimitKeyResolver {

    public String resolveUserKey(HttpServletRequest request) {
        Optional<Long> userIdOpt = SecurityUtils.getCurrentUserId();
        String path = request.getRequestURI();

        if (userIdOpt.isPresent()) {
            return "user:" + userIdOpt.get() + ":" + path;
        }

        String ip = request.getRemoteAddr();
        return "ip:" + ip + ":" + path;
    }

    public String resolveGlobalKey(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "global:" + path;
    }
}
