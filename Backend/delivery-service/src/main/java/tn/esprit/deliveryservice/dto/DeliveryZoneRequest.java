package tn.esprit.deliveryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DeliveryZoneRequest {

    @NotBlank(message = "Zone name is required")
    private String name;

    @Positive(message = "Fee must be positive")
    private double fee;
}
