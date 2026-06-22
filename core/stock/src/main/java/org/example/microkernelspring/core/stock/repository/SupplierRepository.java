package org.example.microkernelspring.core.stock.repository;

import org.example.microkernelspring.core.stock.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID>, JpaSpecificationExecutor<Supplier> {

    List<Supplier> findAllByTenantId(UUID tenantId);;
    Optional<Supplier> findByIdAndTenantId(UUID id, UUID tenantId);

}