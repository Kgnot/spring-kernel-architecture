package org.example.microkernelspring.core.identity.domain.events;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventScope;

import java.time.Instant;
import java.util.UUID;

public record UserLoginFailedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID tenantId,
        UUID userLoginId,
        short failedAttempts
) implements DomainEvent {

    public static final String KEY = "identity.user.login-failed";

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public EventScope scope() {
        return EventScope.INTERNAL;
    }
}