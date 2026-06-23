package org.example.microkernelspring.core.sale.infrastructure.controllers.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        UUID tenantId,
        UUID profileId,
        String companyName,
        String taxId,
        BigDecimal creditLimit) {
}