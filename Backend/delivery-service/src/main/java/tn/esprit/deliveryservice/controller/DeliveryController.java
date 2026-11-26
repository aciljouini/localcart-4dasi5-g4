package tn.esprit.deliveryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.deliveryservice.dto.*;
import tn.esprit.deliveryservice.service.DeliveryService;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // ── Create ──────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<DeliveryResponse> create(@Valid @RequestBody DeliveryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(request));
    }

    // ── Read ────────────────────────────────────────────────────────────────

    /** GET /deliveries — all (ADMIN) */
    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> findAll() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    /** GET /deliveries/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    /** GET /deliveries/order/{orderId} — CUSTOMER, SELLER, ADMIN */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<DeliveryResponse>> findByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(deliveryService.findByOrderId(orderId));
    }

    /** GET /deliveries/seller — SELLER sees their own deliveries */
    @GetMapping("/seller")
    public ResponseEntity<List<DeliveryResponse>> findBySeller(@RequestParam String sellerId) {
        return ResponseEntity.ok(deliveryService.findBySeller(sellerId));
    }

    /** GET /deliveries/{id}/tracking — tracking history */
    @GetMapping("/{id}/tracking")
    public ResponseEntity<TrackingHistoryResponse> tracking(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getTracking(id));
    }

    // ── Assign courier ──────────────────────────────────────────────────────

    /** PUT /deliveries/{id}/assign — ADMIN */
    @PutMapping("/{id}/assign")
    public ResponseEntity<DeliveryResponse> assign(
            @PathVariable Long id,
            @Valid @RequestBody AssignCourierRequest request) {
        return ResponseEntity.ok(deliveryService.assignCourier(id, request));
    }

    // ── Update status ───────────────────────────────────────────────────────

    /** PUT /deliveries/{id}/status — SELLER (PICKED_UP), ADMIN */
    @PutMapping("/{id}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(deliveryService.updateStatus(id, request));
    }

    // ── Quote ───────────────────────────────────────────────────────────────

    /** POST /deliveries/quote */
    @PostMapping("/quote")
    public ResponseEntity<QuoteResponse> quote(@Valid @RequestBody QuoteRequest request) {
        return ResponseEntity.ok(deliveryService.quote(request));
    }

    // ── Delete ──────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
