package tn.esprit.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "deliveries",
    uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "seller_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private DeliveryZone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @Column(name = "delivery_fee", nullable = false)
    private double deliveryFee;

    @Column(nullable = false)
    @Builder.Default
    private int attempts = 0;

    @Column(name = "estimated_at")
    private LocalDateTime estimatedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
