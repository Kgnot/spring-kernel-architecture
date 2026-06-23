package org.example.microkernelspring.core.hr.controller;

import org.example.microkernelspring.core.hr.controller.dto.EmployeeLedgerResponse;
import org.example.microkernelspring.core.hr.service.EmployeeLedgerService;
import org.example.microkernelspring.core.hr.service.dto.EmployeeLedgerFilterDto;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/hr")
public class EmployeeLedgerController {

    private final EmployeeLedgerService employeeLedgerService;

    public EmployeeLedgerController(EmployeeLedgerService employeeLedgerService) {
        this.employeeLedgerService = employeeLedgerService;
    }

    @GetMapping("/ledger")
    public ResponseEntity<Page<EmployeeLedgerResponse>> search(
            @RequestParam(value = "employeeId", required = false) UUID employeeId,
            @RequestParam(value = "from", required = false) LocalDate from,
            @RequestParam(value = "to", required = false) LocalDate to,
            @RequestParam(value = "entryTypeCode", required = false) String entryTypeCode,
            @PageableDefault(
                    size = 25,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        EmployeeLedgerFilterDto filter = new EmployeeLedgerFilterDto(
                tenantId,
                employeeId,
                from,
                to,
                entryTypeCode
        );

        Page<EmployeeLedgerResponse> response = employeeLedgerService
                .search(filter, pageable)
                .map(item -> new EmployeeLedgerResponse(
                        item.id(),
                        item.tenantId(),
                        item.employeeId(),
                        item.entryTypeCode(),
                        item.amount(),
                        item.createdAt(),
                        item.reason()
                ));

        return ResponseEntity.ok(response);
    }
}