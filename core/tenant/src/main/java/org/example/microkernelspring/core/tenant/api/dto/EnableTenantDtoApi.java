package org.example.microkernelspring.core.tenant.api.dto;

import java.util.UUID;

public record EnableTenantDtoApi(
        UUID id,
        String name
) {
}
