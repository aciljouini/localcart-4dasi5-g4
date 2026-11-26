package tn.esprit.deliveryservice.exception;

import tn.esprit.deliveryservice.entity.DeliveryStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(DeliveryStatus from, DeliveryStatus to) {
        super("Transition " + from + " -> " + to + " interdite");
    }
}
