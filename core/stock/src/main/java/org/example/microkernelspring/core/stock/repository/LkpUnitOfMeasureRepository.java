package org.example.microkernelspring.core.stock.repository;

import org.example.microkernelspring.core.stock.entity.LkpUnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpUnitOfMeasureRepository extends JpaRepository<LkpUnitOfMeasure, UUID> {
}