package tn.esprit.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackingHistoryResponse {
    private Long deliveryId;
    private List<TrackingEventResponse> events;
}
