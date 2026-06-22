package org.example.microkernelspring.core.stock.repository;

import org.example.microkernelspring.core.stock.entity.LkpMovementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpMovementTypeRepository extends JpaRepository<LkpMovementType, UUID> {
}