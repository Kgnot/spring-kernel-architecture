package org.example.microkernelspring.core.hr.controller;

import org.example.microkernelspring.core.hr.controller.dto.EmployeeResponse;
import org.example.microkernelspring.core.hr.service.EmployeeService;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hr/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> findByTenant(
            @PageableDefault(size = 20, sort = "id")
            Pageable pageable
    ) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        Page<EmployeeResponse> response = employeeService
                .findByTenant(tenantId, pageable)
                .map(employee -> new EmployeeResponse(
                        employee.id(),
                        employee.tenantId(),
                        employee.firstName(),
                        employee.lastName(),
                        employee.employmentStatusCode()
                ));

        return ResponseEntity.ok(response);
    }
}