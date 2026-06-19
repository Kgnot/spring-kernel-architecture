package org.example.microkernelspring.events;

import java.math.BigDecimal;

public record SaleCreatedEvent(
        Long tenantId,
        BigDecimal amount
) {
}