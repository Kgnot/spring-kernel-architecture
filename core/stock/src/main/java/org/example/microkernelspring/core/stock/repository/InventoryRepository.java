package org.example.microkernelspring.core.stock.repository;

import org.example.microkernelspring.core.stock.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID>, JpaSpecificationExecutor<Inventory> {

    List<Inventory> findAllByTenantId(UUID tenantId);

    Optional<Inventory> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<Inventory> findByTenantIdAndProductIdAndSiteId(UUID tenantId, UUID productId, UUID siteId);


}