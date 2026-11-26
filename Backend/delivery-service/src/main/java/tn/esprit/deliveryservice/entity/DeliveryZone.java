package tn.esprit.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_zones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double fee;
}
