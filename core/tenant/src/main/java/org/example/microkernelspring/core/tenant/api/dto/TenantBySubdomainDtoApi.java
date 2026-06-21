package org.example.microkernelspring.core.tenant.api.dto;

import java.util.UUID;

public record TenantBySubdomainDtoApi(
        UUID id,
        String legalName,
        String tradeName,
        String industry,
        String status,
        String subdomain
) {
}
