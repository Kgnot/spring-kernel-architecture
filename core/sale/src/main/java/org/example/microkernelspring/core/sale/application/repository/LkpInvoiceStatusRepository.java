package org.example.microkernelspring.core.sale.application.repository;

import org.example.microkernelspring.core.sale.domain.entity.LkpInvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpInvoiceStatusRepository extends JpaRepository<LkpInvoiceStatus, UUID> {
}
