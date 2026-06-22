package org.example.microkernelspring.core.stock.repository;

import org.example.microkernelspring.core.stock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByTenantId(UUID tenantId);

    Optional<Product> findByIdAndTenantId(UUID id, UUID tenantId);
}