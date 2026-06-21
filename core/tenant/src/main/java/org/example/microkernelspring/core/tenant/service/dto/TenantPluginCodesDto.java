package org.example.microkernelspring.core.tenant.service.dto;

import java.util.List;
import java.util.UUID;

public record TenantPluginCodesDto(
        UUID tenantId,
        List<String> pluginCodes
) {
}