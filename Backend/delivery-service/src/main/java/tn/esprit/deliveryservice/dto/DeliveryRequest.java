package tn.esprit.deliveryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequest {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotBlank(message = "Seller ID is required")
    private String sellerId;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotNull(message = "Zone ID is required")
    private Long zoneId;
}
