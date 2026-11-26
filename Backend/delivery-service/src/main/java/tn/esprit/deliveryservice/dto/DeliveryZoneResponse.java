package tn.esprit.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryZoneResponse {
    private Long id;
    private String name;
    private double fee;
}
