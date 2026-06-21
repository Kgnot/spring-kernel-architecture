package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpSiteType;
import org.example.microkernelspring.core.tenant.persistence.entity.Site;
import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Repository de Site. Importante paginar: un tenant grande (cadena de
 * tiendas) puede tener decenas o cientos de sedes/bodegas.
 */
public interface SiteRepository extends JpaRepository<Site, UUID> {

    /** Listado paginado de sedes de un tenant, para el módulo de administración. */
    Page<Site> findByTenant(Tenant tenant, Pageable pageable);

    Page<Site> findByTenantAndSiteType(Tenant tenant, LkpSiteType siteType, Pageable pageable);

    Page<Site> findByTenantAndActiveTrue(Tenant tenant, Pageable pageable);

    /** Sin paginar: para combos/selects en UI donde se necesitan todas las sedes activas de un tenant. */
    List<Site> findByTenantIdAndActiveTrueOrderByNameAsc(UUID tenantId);

    /**
     * Búsqueda geoespacial con PostGIS: sedes del tenant dentro de un radio
     * (en metros) desde un punto dado. Útil para "sede más cercana al
     * cliente" o asignación de despacho.
     */
    @Query(value = """
            SELECT * FROM tenant.sites s
            WHERE s.tenant_id = :tenantId
              AND s.is_active = true
              AND ST_DWithin(s.location, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326), :radiusMeters)
            ORDER BY ST_Distance(s.location, ST_SetSRID(ST_MakePoint(:lng, :lat), 4326))
            """, nativeQuery = true)
    List<Site> findNearby(@Param("id") UUID tenantId,
                           @Param("lat") double lat,
                           @Param("lng") double lng,
                           @Param("radiusMeters") double radiusMeters);

    /** Listado paginado de sedes por ciudad (referencia lógica a geo.city), para reportes regionales. */
    Page<Site> findByTenantAndCityId(Tenant tenant, UUID cityId, Pageable pageable);
}
