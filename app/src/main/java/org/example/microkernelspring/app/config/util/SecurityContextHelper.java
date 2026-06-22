package org.example.microkernelspring.app.config.util;

import org.example.microkernelspring.shared.infra.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityContextHelper {

    public static AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            return (AuthenticatedUser) authentication.getPrincipal();
        }
        throw new IllegalStateException("No hay un usuario autenticado en el contexto de seguridad");
    }

    public static UUID getCurrentTenantId() {
        return getAuthenticatedUser().tenantId();
    }

    public static UUID getCurrentUserId() {
        return getAuthenticatedUser().userId();
    }

}
