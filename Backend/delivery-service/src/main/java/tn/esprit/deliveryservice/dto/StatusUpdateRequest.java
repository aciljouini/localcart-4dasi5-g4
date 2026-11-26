package tn.esprit.deliveryservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import tn.esprit.deliveryservice.entity.DeliveryStatus;

@Data
public class StatusUpdateRequest {

    @NotNull(message = "Status is required")
    private DeliveryStatus status;

    private String note;
}
