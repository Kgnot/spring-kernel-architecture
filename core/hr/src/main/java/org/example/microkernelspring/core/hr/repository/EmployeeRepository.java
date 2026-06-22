package org.example.microkernelspring.core.hr.repository;

import org.example.microkernelspring.core.hr.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Page<Employee> findByTenantId(
            UUID tenantId,
            Pageable pageable
    );
}