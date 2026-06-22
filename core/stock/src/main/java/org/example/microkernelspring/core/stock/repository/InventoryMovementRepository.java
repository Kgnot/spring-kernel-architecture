package org.example.microkernelspring.core.stock.repository;

import org.example.microkernelspring.core.stock.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, UUID>, JpaSpecificationExecutor<InventoryMovement> {
    List<InventoryMovement> findAllByTenantId(UUID tenantId);

    Optional<InventoryMovement> findByIdAndTenantId(UUID id, UUID tenantId);
}