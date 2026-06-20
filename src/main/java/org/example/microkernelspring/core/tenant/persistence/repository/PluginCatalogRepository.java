package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.PluginCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PluginCatalogRepository extends JpaRepository<PluginCatalog, String> {
}