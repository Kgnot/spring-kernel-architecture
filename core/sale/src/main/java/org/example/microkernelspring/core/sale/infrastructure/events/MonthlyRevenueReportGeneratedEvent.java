package org.example.microkernelspring.core.sale.infrastructure.events;

import org.example.microkernelspring.shared.application.events.DomainEvent;
import org.example.microkernelspring.shared.application.events.EventScope;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.UUID;

public record MonthlyRevenueReportGeneratedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID tenantId,
        String recipientEmail,
        YearMonth period,
        BigDecimal revenue
) implements DomainEvent {

    public static final String KEY = "sales.monthly-revenue-report.generated";

    public MonthlyRevenueReportGeneratedEvent(
            UUID tenantId,
            String recipientEmail,
            YearMonth period,
            BigDecimal revenue
    ) {
        this(
                UUID.randomUUID(),
                Instant.now(),
                tenantId,
                recipientEmail,
                period,
                revenue
        );
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public EventScope scope() {
        return EventScope.BOTH;
    }
}