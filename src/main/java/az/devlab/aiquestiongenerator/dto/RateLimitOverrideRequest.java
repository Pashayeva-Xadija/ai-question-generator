package az.devlab.aiquestiongenerator.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RateLimitOverrideRequest {

    @NotNull
    private Long userId;

    @Min(1)
    private int perUserRequestsPerMinute;
}

