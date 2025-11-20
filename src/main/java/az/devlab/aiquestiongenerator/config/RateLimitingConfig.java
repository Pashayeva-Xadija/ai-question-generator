package az.devlab.aiquestiongenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "ratelimit")
@Data
public class RateLimitingConfig {

    private boolean enabled = true;

    private int globalRequestsPerMinute = 100;

    private int perUserRequestsPerMinute = 20;

    private int windowSeconds = 60;
}
