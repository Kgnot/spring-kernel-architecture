package org.example.microkernelspring.core.tenant.service.dto;

import java.util.UUID;

public record EnableTenantDto(
        UUID id,
        String name
) {
}
