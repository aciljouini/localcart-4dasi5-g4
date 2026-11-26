package tn.esprit.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "couriers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private DeliveryZone zone;

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;
}
