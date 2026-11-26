package tn.esprit.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.deliveryservice.entity.TrackingEvent;

import java.util.List;

public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {
    List<TrackingEvent> findByDeliveryIdOrderByCreatedAtAsc(Long deliveryId);
}
