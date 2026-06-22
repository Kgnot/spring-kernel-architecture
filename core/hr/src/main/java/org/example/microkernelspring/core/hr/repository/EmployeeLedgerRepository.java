package org.example.microkernelspring.core.hr.repository;

import org.example.microkernelspring.core.hr.entity.EmployeeLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.UUID;

public interface EmployeeLedgerRepository extends JpaRepository<EmployeeLedger, UUID>, JpaSpecificationExecutor<EmployeeLedger> {

    Page<EmployeeLedger> findByTenantIdAndEmployee_Id(UUID tenantId, UUID employeeId, Pageable pageable);

    Page<EmployeeLedger> findByTenantIdAndCreatedAtBetween(UUID tenantId, Instant from, Instant to, Pageable pageable);

    Page<EmployeeLedger> findByTenantIdAndEmployee_IdAndCreatedAtBetween(UUID tenantId, UUID employeeId, Instant from, Instant to, Pageable pageable);

    Page<EmployeeLedger> findByTenantIdAndEntryType_Code(UUID tenantId, String entryTypeCode, Pageable pageable);
}