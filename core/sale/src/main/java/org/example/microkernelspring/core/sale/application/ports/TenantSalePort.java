package org.example.microkernelspring.core.sale.application.ports;

import org.example.microkernelspring.core.tenant.api.TenantApi;
import org.example.microkernelspring.core.tenant.api.dto.EnableTenantDtoApi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantSalePort {

    private final TenantApi tenantApi;

    public TenantSalePort(TenantApi tenantApi) {
        this.tenantApi = tenantApi;
    }

    public List<EnableTenantDtoApi> getEnabledTenants(String pluginId) {
        return tenantApi.getEnabledTenants(pluginId);
    }


}
