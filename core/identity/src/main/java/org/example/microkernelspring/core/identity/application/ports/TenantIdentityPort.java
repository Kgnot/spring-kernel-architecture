package org.example.microkernelspring.core.identity.application.ports;

import org.example.microkernelspring.core.tenant.api.TenantApi;
import org.example.microkernelspring.core.tenant.api.dto.TenantBySubdomainDtoApi;
import org.example.microkernelspring.core.tenant.api.dto.TenantPluginCodesDtoApi;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TenantIdentityPort {

    private final TenantApi tenantApi;

    public TenantIdentityPort(TenantApi tenantApi) {
        this.tenantApi = tenantApi;
    }

    public Optional<TenantBySubdomainDtoApi> findBySubdomain(String subdomain) {
        return tenantApi.findBySubdomain(subdomain);
    }

    public TenantPluginCodesDtoApi getActivePluginCodes(UUID tenantId){
        return tenantApi.getActivePluginCodes(tenantId);
    }

}
