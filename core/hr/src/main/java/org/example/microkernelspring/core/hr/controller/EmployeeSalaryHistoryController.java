package org.example.microkernelspring.core.hr.controller;

import jakarta.validation.Valid;
import org.example.microkernelspring.core.hr.controller.dto.EmployeeSalaryHistoryResponse;
import org.example.microkernelspring.core.hr.controller.dto.RegisterSalaryChangeRequest;
import org.example.microkernelspring.core.hr.service.EmployeeSalaryHistoryService;
import org.example.microkernelspring.core.hr.service.dto.RegisterSalaryChangeDto;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/hr/employees/{employeeId}/salary-history")
public class EmployeeSalaryHistoryController {

    private final EmployeeSalaryHistoryService salaryHistoryService;

    public EmployeeSalaryHistoryController(
            EmployeeSalaryHistoryService salaryHistoryService
    ) {
        this.salaryHistoryService = salaryHistoryService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeSalaryHistoryResponse>> findHistory(
            @PathVariable UUID employeeId,
            @PageableDefault(size = 20, sort = "effectiveDate")
            Pageable pageable
    ) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        Page<EmployeeSalaryHistoryResponse> response = salaryHistoryService
                .findByEmployee(tenantId, employeeId, pageable)
                .map(this::toResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    public ResponseEntity<EmployeeSalaryHistoryResponse> findCurrentSalary(
            @PathVariable UUID employeeId,
            @RequestParam(required = false) LocalDate at
    ) {
        LocalDate date = at != null ? at : LocalDate.now();
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        return ResponseEntity.ok(
                toResponse(
                        salaryHistoryService.findCurrentSalary(
                                tenantId,
                                employeeId,
                                date
                        )
                )
        );
    }

    @PostMapping
    public ResponseEntity<EmployeeSalaryHistoryResponse> registerChange(
            @PathVariable UUID employeeId,
            @Valid @RequestBody RegisterSalaryChangeRequest request
    ) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        RegisterSalaryChangeDto command = new RegisterSalaryChangeDto(
                tenantId,
                employeeId,
                request.salary(),
                request.currency(),
                request.effectiveDate(),
                request.reason()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        toResponse(
                                salaryHistoryService.registerSalaryChange(command)
                        )
                );
    }

    private EmployeeSalaryHistoryResponse toResponse(
            org.example.microkernelspring.core.hr.service.dto.EmployeeSalaryHistoryItemDto item
    ) {
        return new EmployeeSalaryHistoryResponse(
                item.id(),
                item.tenantId(),
                item.employeeId(),
                item.salary(),
                item.currency(),
                item.effectiveDate(),
                item.reason()
        );
    }
}