package org.example.microkernelspring.core.tenant.api;

import org.example.microkernelspring.core.tenant.api.dto.EnableTenantDtoApi;
import org.example.microkernelspring.core.tenant.api.dto.TenantBySubdomainDtoApi;
import org.example.microkernelspring.core.tenant.api.dto.TenantPluginCodesDtoApi;
import org.example.microkernelspring.core.tenant.service.TenantPluginService;
import org.example.microkernelspring.core.tenant.service.TenantService;
import org.example.microkernelspring.core.tenant.service.dto.TenantPluginCodesDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TenantApiImpl implements TenantApi {

    private final TenantPluginService tenantPluginService;
    private final TenantService tenantService;

    public TenantApiImpl(
            TenantPluginService tenantPluginService,
            TenantService tenantService
    ) {
        this.tenantPluginService = tenantPluginService;
        this.tenantService = tenantService;
    }

    @Override
    public List<EnableTenantDtoApi> getEnabledTenants(String pluginId) {
        return tenantPluginService.getEnabledTenants(pluginId)
                .stream()
                .map(tenant -> new EnableTenantDtoApi(
                        tenant.id(),
                        tenant.name()
                ))
                .toList();
    }

    @Override
    public Optional<TenantBySubdomainDtoApi> findBySubdomain(
            String subdomain
    ) {
        return tenantService.findBySubdomain(subdomain)
                .map(tenant -> new TenantBySubdomainDtoApi(
                        tenant.id(),
                        tenant.legalName(),
                        tenant.tradeName(),
                        tenant.industry(),
                        tenant.status(),
                        tenant.subdomain()
                ));
    }

    @Override
    public TenantPluginCodesDtoApi getActivePluginCodes(UUID tenantId) {
        TenantPluginCodesDto result = tenantPluginService.getActivePluginCodes(tenantId);

        return new TenantPluginCodesDtoApi(result.tenantId(), result.pluginCodes());
    }
}