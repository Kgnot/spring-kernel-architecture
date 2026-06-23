package org.example.microkernelspring.core.sale.event;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InvoiceCreatedEvent(
        UUID invoiceId,
        UUID tenantId,
        String invoiceNumber,
        BigDecimal grandTotal
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
        return "invoice.created";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}