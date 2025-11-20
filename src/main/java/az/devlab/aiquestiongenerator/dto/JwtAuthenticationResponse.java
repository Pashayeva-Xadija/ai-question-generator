package az.devlab.aiquestiongenerator.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtAuthenticationResponse {

    private String accessToken;

    private String refreshToken;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long userId;

    private String username;

    private String email;

    private Set<String> roles;
}
