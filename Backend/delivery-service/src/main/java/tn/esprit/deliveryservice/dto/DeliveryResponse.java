package tn.esprit.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;
import tn.esprit.deliveryservice.entity.DeliveryStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryResponse {
    private Long id;
    private String orderId;
    private String sellerId;
    private String customerId;
    private String deliveryAddress;
    private Long zoneId;
    private String zoneName;
    private Long courierId;
    private String courierName;
    private DeliveryStatus status;
    private double deliveryFee;
    private int attempts;
    private LocalDateTime estimatedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
}
