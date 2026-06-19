package org.example.microkernelspring.persistence.repository;

import org.example.microkernelspring.persistence.entity.PluginCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PluginCatalogRepository extends JpaRepository<PluginCatalog, String> {
}