package org.example.microkernelspring.core.identity.domain.events;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

public record UserLoginSucceededEvent(
        UUID eventId,
        Instant occurredAt,
        UUID tenantId,
        UUID userLoginId,
        String email
) implements DomainEvent {

    @Override
    public String key() {
        return "identity.user.login-succeeded";
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}
