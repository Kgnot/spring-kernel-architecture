package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpPluginCategory;
import org.example.microkernelspring.core.tenant.persistence.entity.PluginCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PluginCatalogRepository extends JpaRepository<PluginCatalog, UUID> {

    Optional<PluginCatalog> findByCode(String code);

    /**
     * Plugins activos, sin paginar — usado para poblar el catálogo completo en pantallas de "marketplace de plugins".
     */
    List<PluginCatalog> findByActiveTrueOrderByName();

    /**
     * Listado paginado por categoría, para el catálogo administrable.
     */
    Page<PluginCatalog> findByCategory(LkpPluginCategory category, Pageable pageable);

    Page<PluginCatalog> findByActive(boolean isActive, Pageable pageable);
}
