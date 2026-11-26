package tn.esprit.deliveryservice.service;

import tn.esprit.deliveryservice.entity.DeliveryStatus;
import tn.esprit.deliveryservice.exception.InvalidStatusTransitionException;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Enforces the allowed status transitions defined in the dossier.
 */
public class DeliveryStateMachine {

    private static final Map<DeliveryStatus, Set<DeliveryStatus>> ALLOWED =
            new EnumMap<>(DeliveryStatus.class);

    static {
        ALLOWED.put(DeliveryStatus.PENDING,    EnumSet.of(DeliveryStatus.ASSIGNED,  DeliveryStatus.CANCELLED));
        ALLOWED.put(DeliveryStatus.ASSIGNED,   EnumSet.of(DeliveryStatus.PICKED_UP, DeliveryStatus.CANCELLED));
        ALLOWED.put(DeliveryStatus.PICKED_UP,  EnumSet.of(DeliveryStatus.IN_TRANSIT));
        ALLOWED.put(DeliveryStatus.IN_TRANSIT, EnumSet.of(DeliveryStatus.DELIVERED, DeliveryStatus.FAILED));
        ALLOWED.put(DeliveryStatus.FAILED,     EnumSet.of(DeliveryStatus.ASSIGNED,  DeliveryStatus.RETURNED));
        // Terminal states: no outgoing transitions
        ALLOWED.put(DeliveryStatus.DELIVERED,  EnumSet.noneOf(DeliveryStatus.class));
        ALLOWED.put(DeliveryStatus.RETURNED,   EnumSet.noneOf(DeliveryStatus.class));
        ALLOWED.put(DeliveryStatus.CANCELLED,  EnumSet.noneOf(DeliveryStatus.class));
    }

    public static void validate(DeliveryStatus from, DeliveryStatus to) {
        Set<DeliveryStatus> allowed = ALLOWED.getOrDefault(from, EnumSet.noneOf(DeliveryStatus.class));
        if (!allowed.contains(to)) {
            throw new InvalidStatusTransitionException(from, to);
        }
    }
}
