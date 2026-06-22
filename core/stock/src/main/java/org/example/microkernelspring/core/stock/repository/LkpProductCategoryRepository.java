package org.example.microkernelspring.core.stock.repository;

import org.example.microkernelspring.core.stock.entity.LkpProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpProductCategoryRepository extends JpaRepository<LkpProductCategory, UUID> {
}