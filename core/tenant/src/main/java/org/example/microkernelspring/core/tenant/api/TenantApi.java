package org.example.microkernelspring.core.tenant.api;

import org.example.microkernelspring.core.tenant.api.dto.EnableTenantDtoApi;
import org.example.microkernelspring.core.tenant.api.dto.TenantBySubdomainDtoApi;
import org.example.microkernelspring.core.tenant.api.dto.TenantPluginCodesDtoApi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantApi {
    List<EnableTenantDtoApi> getEnabledTenants(String pluginId);

    Optional<TenantBySubdomainDtoApi> findBySubdomain(String subdomain);

    TenantPluginCodesDtoApi getActivePluginCodes(UUID tenantId);


}
