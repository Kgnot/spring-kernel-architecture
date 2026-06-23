package org.example.microkernelspring.core.sale.persistence.repository;

import org.example.microkernelspring.core.sale.persistence.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}