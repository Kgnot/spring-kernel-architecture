package org.example.microkernelspring.core.hr.repository;

import org.example.microkernelspring.core.hr.entity.LkpEmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LkpEmploymentStatusRepository extends JpaRepository<LkpEmploymentStatus, UUID> {
}
