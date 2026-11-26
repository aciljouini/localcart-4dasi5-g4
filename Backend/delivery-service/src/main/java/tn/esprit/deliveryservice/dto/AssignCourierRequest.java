package tn.esprit.deliveryservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignCourierRequest {

    @NotNull(message = "Courier ID is required")
    private Long courierId;

    private LocalDateTime estimatedAt;
}
