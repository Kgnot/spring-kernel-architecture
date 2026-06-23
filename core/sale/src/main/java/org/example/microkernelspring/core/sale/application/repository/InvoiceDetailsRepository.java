package org.example.microkernelspring.core.sale.application.repository;

import org.example.microkernelspring.core.sale.domain.entity.InvoiceDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, UUID> {

    Page<InvoiceDetails> findByInvoiceId(UUID invoiceId, Pageable pageable);
}
