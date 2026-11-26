package tn.esprit.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.deliveryservice.entity.Courier;

import java.util.List;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    List<Courier> findByZoneIdAndAvailableTrue(Long zoneId);
    List<Courier> findByAvailableTrue();
}
