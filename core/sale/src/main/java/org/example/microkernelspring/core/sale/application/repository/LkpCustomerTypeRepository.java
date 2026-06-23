package org.example.microkernelspring.core.sale.application.repository;

import org.example.microkernelspring.core.sale.domain.entity.LkpCustomerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpCustomerTypeRepository extends JpaRepository<LkpCustomerType, UUID> {
}
