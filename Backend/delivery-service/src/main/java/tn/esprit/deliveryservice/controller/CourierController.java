package tn.esprit.deliveryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.deliveryservice.dto.CourierRequest;
import tn.esprit.deliveryservice.dto.CourierResponse;
import tn.esprit.deliveryservice.service.CourierService;

import java.util.List;

@RestController
@RequestMapping("/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    /** POST /couriers — ADMIN only (role enforcement added with security layer) */
    @PostMapping
    public ResponseEntity<CourierResponse> create(@Valid @RequestBody CourierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courierService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CourierResponse>> findAll() {
        return ResponseEntity.ok(courierService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(courierService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourierResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CourierRequest request) {
        return ResponseEntity.ok(courierService.update(id, request));
    }

    /** PATCH /couriers/{id}/availability?available=true|false */
    @PatchMapping("/{id}/availability")
    public ResponseEntity<CourierResponse> setAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        return ResponseEntity.ok(courierService.setAvailability(id, available));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
