package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpPluginStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository de LkpPluginStatus. Catálogo pequeño y estático, sin paginación. */
public interface LkpPluginStatusRepository extends JpaRepository<LkpPluginStatus, UUID> {

    Optional<LkpPluginStatus> findByCode(String code);

    List<LkpPluginStatus> findAllByOrderByName();
}
