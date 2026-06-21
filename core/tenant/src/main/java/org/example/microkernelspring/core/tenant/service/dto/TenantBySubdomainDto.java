package org.example.microkernelspring.core.tenant.service.dto;

import java.util.UUID;

public record TenantBySubdomainDto(
        UUID tenantId,
        String subdomain
) {
}