package org.example.microkernelspring.core.sale.application.repository;

import org.example.microkernelspring.core.sale.domain.entity.LkpPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpPaymentMethodRepository extends JpaRepository<LkpPaymentMethod, UUID> {
}
