package tn.esprit.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.deliveryservice.dto.DeliveryZoneRequest;
import tn.esprit.deliveryservice.dto.DeliveryZoneResponse;
import tn.esprit.deliveryservice.entity.DeliveryZone;
import tn.esprit.deliveryservice.exception.ResourceNotFoundException;
import tn.esprit.deliveryservice.repository.DeliveryZoneRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryZoneService {

    private final DeliveryZoneRepository zoneRepository;

    public DeliveryZoneResponse create(DeliveryZoneRequest request) {
        DeliveryZone zone = DeliveryZone.builder()
                .name(request.getName())
                .fee(request.getFee())
                .build();
        return toResponse(zoneRepository.save(zone));
    }

    public List<DeliveryZoneResponse> findAll() {
        return zoneRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DeliveryZoneResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public DeliveryZoneResponse update(Long id, DeliveryZoneRequest request) {
        DeliveryZone zone = getOrThrow(id);
        zone.setName(request.getName());
        zone.setFee(request.getFee());
        return toResponse(zoneRepository.save(zone));
    }

    public void delete(Long id) {
        getOrThrow(id);
        zoneRepository.deleteById(id);
    }

    // ── package-visible helper used by other services ──────────────────────
    DeliveryZone getOrThrow(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found: " + id));
    }

    DeliveryZone findByCity(String city) {
        return zoneRepository.findByNameIgnoreCase(city)
                .orElseThrow(() -> new ResourceNotFoundException("No zone found for city: " + city));
    }

    private DeliveryZoneResponse toResponse(DeliveryZone zone) {
        return DeliveryZoneResponse.builder()
                .id(zone.getId())
                .name(zone.getName())
                .fee(zone.getFee())
                .build();
    }
}
