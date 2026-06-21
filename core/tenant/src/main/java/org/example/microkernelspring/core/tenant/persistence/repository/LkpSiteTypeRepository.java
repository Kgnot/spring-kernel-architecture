package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpSiteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository de LkpSiteType. Catálogo pequeño y estático, sin paginación. */
public interface LkpSiteTypeRepository extends JpaRepository<LkpSiteType, UUID> {

    Optional<LkpSiteType> findByCode(String code);

    List<LkpSiteType> findAllByOrderByName();
}
