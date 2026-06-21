package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.example.microkernelspring.core.tenant.persistence.entity.TenantPartnerLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface TenantPartnerLinkRepository extends JpaRepository<TenantPartnerLink, UUID> {

    Page<TenantPartnerLink> findByTenant(Tenant tenant, Pageable pageable);

    /**
     * La relación puede consultarse desde cualquiera de los dos lados
     * (tenant o partnerTenant); esta consulta cubre ambos sentidos sin
     * paginar, pensada para resolver permisos de visibilidad cruzada
     * (ej. logistics.shipment.sharedWithTenantId) en caliente.
     */
    @Query("""
            SELECT l FROM TenantPartnerLink l
            WHERE l.active = true
              AND (l.tenant.id = :tenantId OR l.partnerTenant.id = :tenantId)
            """)
    List<TenantPartnerLink> findActiveLinksInvolvingTenant(@Param("tenantId") UUID tenantId);

    Page<TenantPartnerLink> findByTenantAndActiveTrue(Tenant tenant, Pageable pageable);
}
