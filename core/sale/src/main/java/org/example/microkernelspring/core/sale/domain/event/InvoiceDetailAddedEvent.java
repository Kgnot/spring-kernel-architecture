package org.example.microkernelspring.core.sale.domain.event;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InvoiceDetailAddedEvent(
        UUID invoiceDetailId,
        UUID invoiceId,
        BigDecimal lineTotal
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
        return "invoice-detail.added";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}