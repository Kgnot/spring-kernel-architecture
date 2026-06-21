package org.example.microkernelspring.core.tenant.api.dto;

import java.util.List;
import java.util.UUID;

public record TenantPluginCodesDtoApi(
        UUID tenantId,
        List<String> pluginCodes
) {
}