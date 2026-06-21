package org.example.microkernelspring.core.tenant.service;

import org.example.microkernelspring.core.tenant.persistence.repository.TenantPluginRepository;
import org.example.microkernelspring.core.tenant.service.dto.EnableTenantDto;
import org.example.microkernelspring.core.tenant.service.dto.TenantPluginCodesDto;
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

    public List<EnableTenantDto> getEnabledTenants(String pluginId) {
        return repository.findEnabledTenantsByPlugin(pluginId)
                .stream()
                .map(tenant -> new EnableTenantDto(
                        tenant.getId(),
                        tenant.getLegalName()
                ))
                .toList();
    }

    /**
     * Contrato de aplicación: devuelve los códigos activos del tenant.
     * No expone entidades JPA ni objetos Plugin.
     */
    public TenantPluginCodesDto getActivePluginCodes(UUID tenantId) {
        List<String> pluginCodes = repository.findActivePluginCodesByTenantId(tenantId);

        return new TenantPluginCodesDto(
                tenantId,
                pluginCodes
        );
    }

    /**
     * Uso interno del kernel: resuelve plugins Java cargados en memoria.
     * Este mét no debería exponerse por TenantApi hacia otros módulos.
     */
    public List<Plugin> getActivePluginsByType(
            UUID tenantId,
            PluginType type
    ) {
        return repository.findActivePluginCodesByTenantId(tenantId)
                .stream()
                .map(registry::findById)
                .flatMap(Optional::stream)
                .filter(plugin -> plugin.getType() == type)
                .toList();
    }
}