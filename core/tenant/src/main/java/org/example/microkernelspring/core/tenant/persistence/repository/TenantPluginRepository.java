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

    Optional<TenantPlugin> findByTenantAndPlugin_Code(Tenant tenant, String pluginCode);

    /**
     * Acceso rápido sin paginar: resuelve qué plugins activos tiene un
     * tenant, pensado para cachear/usar en cada request como feature flags.
     */
    @Query("""
            SELECT tp FROM TenantPlugin tp
            WHERE tp.tenant.id = :tenantId AND tp.status.code = 'active'
            """)
    List<TenantPlugin> findActiveByTenantId(@Param("tenantId") UUID tenantId);

    Page<TenantPlugin> findByStatus(LkpPluginStatus status, Pageable pageable);

    boolean existsByTenantIdAndPluginIdAndStatus_Code(UUID tenantId, UUID pluginId, String statusCode);

    @Query("""
            SELECT tp.tenant FROM TenantPlugin tp
            WHERE tp.plugin.code = :pluginId
              AND tp.status.code = 'active'
            """)
    List<Tenant> findEnabledTenantsByPlugin(@Param("pluginId") String pluginId);

    @Query("""
            SELECT tp.plugin.code FROM TenantPlugin tp
            WHERE tp.tenant.id = :tenantId AND tp.status.code = 'active'
            """)
    List<String> findActivePluginCodesByTenantId(@Param("tenantId") UUID tenantId);
}
