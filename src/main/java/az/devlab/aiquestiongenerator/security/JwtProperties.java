package az.devlab.aiquestiongenerator.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String secretKey;

    private long accessTokenExpirationMs = 900_000;

    private long refreshTokenExpirationMs = 1_209_600_000L;
}
