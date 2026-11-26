package tn.esprit.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.deliveryservice.entity.Delivery;
import tn.esprit.deliveryservice.entity.DeliveryStatus;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByOrderId(String orderId);
    List<Delivery> findBySellerId(String sellerId);
    Optional<Delivery> findByOrderIdAndSellerId(String orderId, String sellerId);
    List<Delivery> findByStatus(DeliveryStatus status);
}
