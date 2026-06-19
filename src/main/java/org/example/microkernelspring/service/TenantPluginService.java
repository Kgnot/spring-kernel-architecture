package org.example.microkernelspring.service;

import org.example.microkernelspring.kernel.Plugin;
import org.example.microkernelspring.kernel.PluginRegistry;
import org.example.microkernelspring.kernel.PluginType;
import org.example.microkernelspring.persistence.entity.Tenant;
import org.example.microkernelspring.persistence.repository.TenantPluginRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Plugin> getActivePlugins(Long tenantId) {
        return repository.findEnabledPluginIds(tenantId)
                .stream()
                .map(registry::findById)
                .flatMap(Optional::stream)
                .toList();
    }

    public List<Plugin> getActivePluginsByType(
            Long tenantId,
            PluginType type
    ) {
        return getActivePlugins(tenantId)
                .stream()
                .filter(plugin -> plugin.getType() == type)
                .toList();
    }
}