package org.example.microkernelspring.core.tenant.service;

import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.example.microkernelspring.core.tenant.persistence.repository.TenantPluginRepository;
import org.example.microkernelspring.shared.application.kernel.Plugin;
import org.example.microkernelspring.shared.application.kernel.PluginRegistry;
import org.example.microkernelspring.shared.application.kernel.PluginType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantPluginService {

    private final TenantPluginRepository repository;
    private final PluginRegistry registry;

    public TenantPluginService(
            TenantPluginRepository repository,
            PluginRegistry registry
    ) {
        this.repository = repository;
        this.registry = registry;
    }

    public List<Tenant> getEnabledTenants(String pluginId) {
        return repository.findEnabledTenantsByPlugin(pluginId);
    }

    public List<Plugin> getActivePlugins(UUID tenantId) {
        return repository.findActiveByTenantId(tenantId)
                .stream()
                .map(tenantPlugin -> registry.findById(tenantPlugin.getPlugin().getCode()))
                .flatMap(Optional::stream)
                .toList();
    }

    public List<Plugin> getActivePluginsByType(
            UUID tenantId,
            PluginType type
    ) {
        return getActivePlugins(tenantId)
                .stream()
                .filter(plugin -> plugin.getType() == type)
                .toList();
    }
}