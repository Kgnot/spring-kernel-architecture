package org.example.microkernelspring.core.identity.domain.events;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        Instant occurredAt,
        UUID tenantId,
        UUID userLoginId,
        UUID profileId,
        String email,
        String fullName
) implements DomainEvent {

    @Override
    public String key() {
        return "identity.user.registered";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}
