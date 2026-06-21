package org.example.microkernelspring.core.identity.application.usecase.result;

import java.util.List;
import java.util.UUID;

public record LoginResult(
        UUID userLoginId,
        UUID tenantId,
        String tenantSubdomain,
        String email,
        String fullName,
        List<String> roles,
        List<String> activePluginCodes
) {
}