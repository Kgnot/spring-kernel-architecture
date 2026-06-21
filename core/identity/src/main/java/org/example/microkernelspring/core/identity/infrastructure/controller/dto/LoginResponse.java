package org.example.microkernelspring.core.identity.infrastructure.controller.dto;

import java.util.List;
import java.util.UUID;

public record LoginResponse(
        UUID userLoginId,
        UUID tenantId,
        String tenantSubdomain,
        String email,
        String fullName,
        List<String> roles,
        List<String> activePluginCodes
) {
}
