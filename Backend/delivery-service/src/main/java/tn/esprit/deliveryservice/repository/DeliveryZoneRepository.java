package tn.esprit.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.deliveryservice.entity.DeliveryZone;

import java.util.Optional;

public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
    Optional<DeliveryZone> findByNameIgnoreCase(String name);
}
