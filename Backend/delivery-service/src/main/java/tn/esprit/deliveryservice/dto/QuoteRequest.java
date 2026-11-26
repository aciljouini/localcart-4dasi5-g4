package tn.esprit.deliveryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuoteRequest {

    @NotBlank(message = "City is required")
    private String city;

    @Positive(message = "Seller count must be at least 1")
    private int sellerCount;
}
