package org.example.microkernelspring.core.tenant.service.dto;

import java.time.Instant;
import java.util.UUID;

public record TenantDto(
        UUID id,
        String legalName,
        String tradeName,
        String taxId,
        String industry,
        String status,
        String subdomain,
        String timezone,
        String defaultCurrency,
        Instant trialEndsAt,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
