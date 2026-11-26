package tn.esprit.deliveryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.deliveryservice.dto.DeliveryZoneRequest;
import tn.esprit.deliveryservice.dto.DeliveryZoneResponse;
import tn.esprit.deliveryservice.service.DeliveryZoneService;

import java.util.List;

@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class DeliveryZoneController {

    private final DeliveryZoneService zoneService;

    @PostMapping
    public ResponseEntity<DeliveryZoneResponse> create(@Valid @RequestBody DeliveryZoneRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryZoneResponse>> findAll() {
        return ResponseEntity.ok(zoneService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryZoneResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryZoneResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryZoneRequest request) {
        return ResponseEntity.ok(zoneService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
