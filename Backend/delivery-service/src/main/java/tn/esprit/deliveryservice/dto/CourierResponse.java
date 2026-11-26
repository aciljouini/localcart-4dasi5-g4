package tn.esprit.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierResponse {
    private Long id;
    private String name;
    private String phone;
    private Long zoneId;
    private String zoneName;
    private boolean available;
}
