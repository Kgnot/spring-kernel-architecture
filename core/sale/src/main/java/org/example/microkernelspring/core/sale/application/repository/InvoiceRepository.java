package org.example.microkernelspring.core.sale.application.repository;

import org.example.microkernelspring.core.sale.domain.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    Page<Invoice> findAll(Pageable pageable);

    Page<Invoice> findByTenantId(UUID tenantId, Pageable pageable);

    Page<Invoice> findByCustomerId(UUID customerId, Pageable pageable);
}
