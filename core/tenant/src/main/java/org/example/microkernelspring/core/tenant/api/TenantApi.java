package org.example.microkernelspring.core.tenant.api;

import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;

import java.util.List;

public interface TenantApi {
    List<Tenant> getEnabledTenants(String pluginId);

}
