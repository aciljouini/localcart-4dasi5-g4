package tn.esprit.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.deliveryservice.dto.*;
import tn.esprit.deliveryservice.entity.*;
import tn.esprit.deliveryservice.exception.ResourceNotFoundException;
import tn.esprit.deliveryservice.repository.DeliveryRepository;
import tn.esprit.deliveryservice.repository.TrackingEventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private static final int MAX_ATTEMPTS = 3;

    private final DeliveryRepository deliveryRepository;
    private final TrackingEventRepository trackingEventRepository;
    private final DeliveryZoneService zoneService;
    private final CourierService courierService;

    // ── Create ──────────────────────────────────────────────────────────────

    @Transactional
    public DeliveryResponse create(DeliveryRequest request) {
        DeliveryZone zone = zoneService.getOrThrow(request.getZoneId());

        Delivery delivery = Delivery.builder()
                .orderId(request.getOrderId())
                .sellerId(request.getSellerId())
                .customerId(request.getCustomerId())
                .deliveryAddress(request.getDeliveryAddress())
                .zone(zone)
                .status(DeliveryStatus.PENDING)
                .deliveryFee(zone.getFee())
                .attempts(0)
                .createdAt(LocalDateTime.now())
                .build();

        delivery = deliveryRepository.save(delivery);
        recordTracking(delivery, DeliveryStatus.PENDING, "Livraison créée");
        return toResponse(delivery);
    }

    // ── Read ────────────────────────────────────────────────────────────────

    public DeliveryResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public List<DeliveryResponse> findByOrderId(String orderId) {
        return deliveryRepository.findByOrderId(orderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponse> findBySeller(String sellerId) {
        return deliveryRepository.findBySellerId(sellerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<DeliveryResponse> findAll() {
        return deliveryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Assign courier ──────────────────────────────────────────────────────

    @Transactional
    public DeliveryResponse assignCourier(Long deliveryId, AssignCourierRequest request) {
        Delivery delivery = getOrThrow(deliveryId);
        DeliveryStateMachine.validate(delivery.getStatus(), DeliveryStatus.ASSIGNED);

        Courier courier = courierService.getOrThrow(request.getCourierId());
        delivery.setCourier(courier);
        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setEstimatedAt(request.getEstimatedAt());

        delivery = deliveryRepository.save(delivery);
        recordTracking(delivery, DeliveryStatus.ASSIGNED, "Livreur affecté : " + courier.getName());
        return toResponse(delivery);
    }

    // ── Update status ───────────────────────────────────────────────────────

    @Transactional
    public DeliveryResponse updateStatus(Long deliveryId, StatusUpdateRequest request) {
        Delivery delivery = getOrThrow(deliveryId);
        DeliveryStatus next = request.getStatus();

        DeliveryStateMachine.validate(delivery.getStatus(), next);

        // Business rule: after 3 failed attempts -> RETURNED only
        if (next == DeliveryStatus.ASSIGNED && delivery.getStatus() == DeliveryStatus.FAILED) {
            if (delivery.getAttempts() >= MAX_ATTEMPTS) {
                throw new IllegalArgumentException(
                        "Maximum attempts (" + MAX_ATTEMPTS + ") reached. Delivery must be RETURNED.");
            }
            delivery.setAttempts(delivery.getAttempts() + 1);
        }

        if (next == DeliveryStatus.DELIVERED) {
            delivery.setDeliveredAt(LocalDateTime.now());
        }

        delivery.setStatus(next);
        delivery = deliveryRepository.save(delivery);
        recordTracking(delivery, next, request.getNote());
        return toResponse(delivery);
    }

    // ── Tracking history ────────────────────────────────────────────────────

    public TrackingHistoryResponse getTracking(Long deliveryId) {
        getOrThrow(deliveryId); // ensures delivery exists
        List<TrackingEventResponse> events = trackingEventRepository
                .findByDeliveryIdOrderByCreatedAtAsc(deliveryId)
                .stream()
                .map(this::toEventResponse)
                .collect(Collectors.toList());

        return TrackingHistoryResponse.builder()
                .deliveryId(deliveryId)
                .events(events)
                .build();
    }

    // ── Quote ───────────────────────────────────────────────────────────────

    public QuoteResponse quote(QuoteRequest request) {
        DeliveryZone zone = zoneService.findByCity(request.getCity());
        return QuoteResponse.builder()
                .zone(zone.getName())
                .feePerParcel(zone.getFee())
                .totalFee(zone.getFee() * request.getSellerCount())
                .build();
    }

    // ── Delete (admin use) ──────────────────────────────────────────────────

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        trackingEventRepository.deleteAll(
                trackingEventRepository.findByDeliveryIdOrderByCreatedAtAsc(id));
        deliveryRepository.deleteById(id);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private Delivery getOrThrow(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found: " + id));
    }

    private void recordTracking(Delivery delivery, DeliveryStatus status, String note) {
        TrackingEvent event = TrackingEvent.builder()
                .delivery(delivery)
                .status(status)
                .note(note)
                .createdAt(LocalDateTime.now())
                .build();
        trackingEventRepository.save(event);
    }

    private DeliveryResponse toResponse(Delivery d) {
        return DeliveryResponse.builder()
                .id(d.getId())
                .orderId(d.getOrderId())
                .sellerId(d.getSellerId())
                .customerId(d.getCustomerId())
                .deliveryAddress(d.getDeliveryAddress())
                .zoneId(d.getZone() != null ? d.getZone().getId() : null)
                .zoneName(d.getZone() != null ? d.getZone().getName() : null)
                .courierId(d.getCourier() != null ? d.getCourier().getId() : null)
                .courierName(d.getCourier() != null ? d.getCourier().getName() : null)
                .status(d.getStatus())
                .deliveryFee(d.getDeliveryFee())
                .attempts(d.getAttempts())
                .estimatedAt(d.getEstimatedAt())
                .deliveredAt(d.getDeliveredAt())
                .createdAt(d.getCreatedAt())
                .build();
    }

    private TrackingEventResponse toEventResponse(TrackingEvent e) {
        return TrackingEventResponse.builder()
                .id(e.getId())
                .deliveryId(e.getDelivery().getId())
                .status(e.getStatus())
                .note(e.getNote())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
