package org.example.microkernelspring.persistence.repository;

import org.example.microkernelspring.persistence.entity.Tenant;
import org.example.microkernelspring.persistence.entity.TenantPlugin;
import org.example.microkernelspring.persistence.entity.TenantPluginId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TenantPluginRepository
        extends JpaRepository<TenantPlugin, TenantPluginId> {

    @Query("""
            select tp.plugin.id
            from TenantPlugin tp
            where tp.tenant.id = :tenantId
              and tp.enabled = true
            """)
    List<String> findEnabledPluginIds(Long tenantId);

    @Query("""
            select tp.tenant
            from TenantPlugin tp
            where tp.plugin.id = :pluginId
              and tp.enabled = true
            """)
    List<Tenant> findEnabledTenantsByPlugin(String pluginId);

    List<TenantPlugin> findByTenant_IdAndEnabledTrue(Long tenantId);

}