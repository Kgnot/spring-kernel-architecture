package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpTenantStatus;
import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository de Tenant. Es la tabla raíz: se espera que crezca de forma
 * acotada (uno por cliente de la plataforma), pero igual se pagina el
 * listado general porque un panel de super-admin puede llegar a tener
 * miles de tenants.
 */
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    /** Usado para resolver el tenant en el login a partir del subdominio. */
    Optional<Tenant> findBySubdomain(String subdomain);

    Optional<Tenant> findByTaxId(String taxId);

    boolean existsBySubdomain(String subdomain);

    /** Listado paginado por estado, para el panel de administración de la plataforma. */
    Page<Tenant> findByStatus(LkpTenantStatus status, Pageable pageable);

    /** Búsqueda combinada estado + industria, paginada. */
    Page<Tenant> findByStatusAndIndustryId(LkpTenantStatus status, UUID industryId, Pageable pageable);

    /** Búsqueda libre por nombre legal o comercial, paginada (autocomplete de soporte/ventas). */
    @Query("""
            SELECT t FROM Tenant t
            WHERE LOWER(t.legalName) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(t.tradeName) LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    Page<Tenant> searchByName(@Param("term") String term, Pageable pageable);

    /** Tenants cuyo periodo de prueba ya venció y siguen en estado trial, para job de expiración. */
    @Query("""
            SELECT t FROM Tenant t
            WHERE t.status.code = 'trial' AND t.trialEndsAt < CURRENT_TIMESTAMP
            """)
    Page<Tenant> findExpiredTrials(Pageable pageable);
}
