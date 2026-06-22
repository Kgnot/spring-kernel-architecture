package org.example.microkernelspring.core.hr.repository;

import org.example.microkernelspring.core.hr.entity.EmployeeSalaryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeSalaryHistoryRepository extends JpaRepository<EmployeeSalaryHistory, UUID> {

    Page<EmployeeSalaryHistory> findByTenantIdAndEmployee_Id(UUID tenantId, UUID employeeId, Pageable pageable);

    Optional<EmployeeSalaryHistory>

    findFirstByTenantIdAndEmployee_IdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(UUID tenantId, UUID employeeId, LocalDate date);
}