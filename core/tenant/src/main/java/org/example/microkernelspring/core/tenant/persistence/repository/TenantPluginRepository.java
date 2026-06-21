package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpPluginStatus;
import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.example.microkernelspring.core.tenant.persistence.entity.TenantPlugin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TenantPluginRepository extends JpaRepository<TenantPlugin, UUID> {

    Page<TenantPlugin> findByTenant(Tenant tenant, Pageable pageable);

    Optional<TenantPlugin> findByTenantAndPlugin_Code(
            Tenant tenant,
            String pluginCode
    );

    @Query("""
            SELECT tp FROM TenantPlugin tp
            WHERE tp.tenant.id = :tenantId
              AND tp.status.code = 'active'
            """)
    List<TenantPlugin> findActiveByTenantId(@Param("tenantId") UUID tenantId);

    @Query("""
            SELECT tp.plugin.code FROM TenantPlugin tp
            WHERE tp.tenant.id = :tenantId
              AND tp.status.code = 'active'
            """)
    List<String> findActivePluginCodesByTenantId(@Param("tenantId") UUID tenantId);

    @Query("""
            SELECT tp.tenant FROM TenantPlugin tp
            WHERE tp.plugin.code = :pluginId
              AND tp.status.code = 'active'
            """)
    List<Tenant> findEnabledTenantsByPlugin(@Param("pluginId") String pluginId);
}
