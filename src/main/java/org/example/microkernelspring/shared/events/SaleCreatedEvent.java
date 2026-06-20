package org.example.microkernelspring.shared.events;

import java.math.BigDecimal;

public record SaleCreatedEvent(
        Long tenantId,
        BigDecimal amount
) {
}