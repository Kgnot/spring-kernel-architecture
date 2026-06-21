package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpIndustry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository de LkpIndustry. Catálogo pequeño y estático, sin paginación. */
public interface LkpIndustryRepository extends JpaRepository<LkpIndustry, UUID> {

    Optional<LkpIndustry> findByCode(String code);

    List<LkpIndustry> findAllByOrderByName();
}
