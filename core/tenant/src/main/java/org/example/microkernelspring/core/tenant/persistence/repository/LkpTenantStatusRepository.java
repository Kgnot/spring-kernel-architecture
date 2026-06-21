package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpTenantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository de LkpTenantStatus. Catálogo pequeño y estático: no se pagina,
 * se carga completo (típicamente cacheado en la capa de servicio).
 */
public interface LkpTenantStatusRepository extends JpaRepository<LkpTenantStatus, UUID> {

    Optional<LkpTenantStatus> findByCode(String code);

    List<LkpTenantStatus> findAllByOrderByName();
}
