package az.devlab.aiquestiongenerator.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static Optional<Long> getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails details)) {
            return Optional.empty();
        }
        return Optional.of(details.getId());
    }

    public static Optional<String> getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails details)) {
            return Optional.empty();
        }
        return Optional.of(details.getUsername());
    }
}
