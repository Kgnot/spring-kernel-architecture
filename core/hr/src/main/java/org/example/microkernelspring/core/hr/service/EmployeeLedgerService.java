package org.example.microkernelspring.core.hr.service;

import jakarta.persistence.criteria.Predicate;
import org.example.microkernelspring.core.hr.entity.EmployeeLedger;
import org.example.microkernelspring.core.hr.repository.EmployeeLedgerRepository;
import org.example.microkernelspring.core.hr.service.dto.EmployeeLedgerFilterDto;
import org.example.microkernelspring.core.hr.service.dto.EmployeeLedgerItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class EmployeeLedgerService {

    private final EmployeeLedgerRepository employeeLedgerRepository;

    public EmployeeLedgerService(
            EmployeeLedgerRepository employeeLedgerRepository
    ) {
        this.employeeLedgerRepository = employeeLedgerRepository;
    }

    public Page<EmployeeLedgerItemDto> search(EmployeeLedgerFilterDto filter, Pageable pageable) {
        if (filter.tenantId() == null) {
            throw new IllegalArgumentException("tenantId es obligatorio.");
        }

        Specification<EmployeeLedger> specification = (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(cb.equal(root.get("tenantId"), filter.tenantId()));

            if (filter.employeeId() != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), filter.employeeId()));
            }

            if (filter.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("effectiveDate"), filter.from()));
            }

            if (filter.to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("effectiveDate"), filter.to()));
            }

            if (filter.entryTypeCode() != null && !filter.entryTypeCode().isBlank()) {
                predicates.add(cb.equal(root.get("entryType").get("code"), filter.entryTypeCode()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };

        return employeeLedgerRepository.findAll(specification, pageable)
                .map(this::toItemDto);
    }


    private EmployeeLedgerItemDto toItemDto(EmployeeLedger ledger) {
        return new EmployeeLedgerItemDto(
                ledger.getId(),
                ledger.getTenantId(),
                ledger.getEmployee().getId(),
                ledger.getEntryType().getCode(),
                ledger.getAmount(),
                ledger.getCreatedAt(),
                ledger.getReason()
        );
    }
}