package tn.esprit.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.deliveryservice.dto.CourierRequest;
import tn.esprit.deliveryservice.dto.CourierResponse;
import tn.esprit.deliveryservice.entity.Courier;
import tn.esprit.deliveryservice.entity.DeliveryZone;
import tn.esprit.deliveryservice.exception.ResourceNotFoundException;
import tn.esprit.deliveryservice.repository.CourierRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final DeliveryZoneService zoneService;

    public CourierResponse create(CourierRequest request) {
        DeliveryZone zone = zoneService.getOrThrow(request.getZoneId());
        Courier courier = Courier.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .zone(zone)
                .available(true)
                .build();
        return toResponse(courierRepository.save(courier));
    }

    public List<CourierResponse> findAll() {
        return courierRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CourierResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public CourierResponse update(Long id, CourierRequest request) {
        Courier courier = getOrThrow(id);
        DeliveryZone zone = zoneService.getOrThrow(request.getZoneId());
        courier.setName(request.getName());
        courier.setPhone(request.getPhone());
        courier.setZone(zone);
        return toResponse(courierRepository.save(courier));
    }

    public CourierResponse setAvailability(Long id, boolean available) {
        Courier courier = getOrThrow(id);
        courier.setAvailable(available);
        return toResponse(courierRepository.save(courier));
    }

    public void delete(Long id) {
        getOrThrow(id);
        courierRepository.deleteById(id);
    }

    // ── package-visible helpers ─────────────────────────────────────────────
    Courier getOrThrow(Long id) {
        return courierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found: " + id));
    }

    private CourierResponse toResponse(Courier c) {
        return CourierResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .phone(c.getPhone())
                .zoneId(c.getZone() != null ? c.getZone().getId() : null)
                .zoneName(c.getZone() != null ? c.getZone().getName() : null)
                .available(c.isAvailable())
                .build();
    }
}
