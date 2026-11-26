package tn.esprit.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.deliveryservice.entity.DeliveryStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class TrackingEventResponse {
    private Long id;
    private Long deliveryId;
    private DeliveryStatus status;
    private String note;
    private LocalDateTime createdAt;
}
