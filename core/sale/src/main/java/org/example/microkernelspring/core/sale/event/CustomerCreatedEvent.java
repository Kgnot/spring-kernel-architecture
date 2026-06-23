package org.example.microkernelspring.core.sale.event;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

public record CustomerCreatedEvent(
        UUID customerId,
        UUID tenantId,
        String taxId
) implements DomainEvent {

    @Override
    public UUID eventId() {
        return UUID.randomUUID();
    }

    @Override
    public Instant occurredAt() {
        return Instant.now();
    }

    @Override
    public String key() {
        return "customer.created";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}