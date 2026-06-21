package org.example.microkernelspring.shared.application.events;

import java.math.BigDecimal;

public record SaleCreatedEvent(
        Long tenantId,
        BigDecimal amount
) {
}