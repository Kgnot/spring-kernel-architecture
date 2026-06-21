package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpPluginCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository de LkpPluginCategory. Catálogo pequeño y estático, sin paginación. */
public interface LkpPluginCategoryRepository extends JpaRepository<LkpPluginCategory, UUID> {

    Optional<LkpPluginCategory> findByCode(String code);

    List<LkpPluginCategory> findAllByOrderByName();
}
