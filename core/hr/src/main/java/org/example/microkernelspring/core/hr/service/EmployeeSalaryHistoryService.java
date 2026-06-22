package org.example.microkernelspring.core.hr.service;

import org.example.microkernelspring.core.hr.entity.Employee;
import org.example.microkernelspring.core.hr.entity.EmployeeSalaryHistory;
import org.example.microkernelspring.core.hr.repository.EmployeeSalaryHistoryRepository;
import org.example.microkernelspring.core.hr.service.dto.EmployeeSalaryHistoryItemDto;
import org.example.microkernelspring.core.hr.service.dto.RegisterSalaryChangeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class EmployeeSalaryHistoryService {

    private final EmployeeSalaryHistoryRepository salaryHistoryRepository;
    private final EmployeeService employeeService;

    public EmployeeSalaryHistoryService(
            EmployeeSalaryHistoryRepository salaryHistoryRepository,
            EmployeeService employeeService
    ) {
        this.salaryHistoryRepository = salaryHistoryRepository;
        this.employeeService = employeeService;
    }

    public Page<EmployeeSalaryHistoryItemDto> findByEmployee(
            UUID tenantId,
            UUID employeeId,
            Pageable pageable
    ) {
        return salaryHistoryRepository
                .findByTenantIdAndEmployee_Id(tenantId, employeeId, pageable)
                .map(this::toItemDto);
    }

    public EmployeeSalaryHistoryItemDto findCurrentSalary(
            UUID tenantId,
            UUID employeeId,
            LocalDate date
    ) {
        return salaryHistoryRepository
                .findFirstByTenantIdAndEmployee_IdAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                        tenantId,
                        employeeId,
                        date
                )
                .map(this::toItemDto)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe historial salarial vigente para el empleado."
                ));
    }

    @Transactional
    public EmployeeSalaryHistoryItemDto registerSalaryChange(
            RegisterSalaryChangeDto command
    ) {
        validateSalary(command);

        Employee employee = employeeService.findEntityByIdAndTenant(
                command.employeeId(),
                command.tenantId()
        );

        EmployeeSalaryHistory history = new EmployeeSalaryHistory();
        history.setTenantId(command.tenantId());
        history.setEmployee(employee);
        history.setSalary(command.salary());
        history.setCurrency(command.currency().toUpperCase());
        history.setEffectiveDate(command.effectiveDate());
        history.setReason(command.reason());

        EmployeeSalaryHistory saved = salaryHistoryRepository.save(history);

        return toItemDto(saved);
    }

    private void validateSalary(RegisterSalaryChangeDto command) {
        if (command.salary() == null || command.salary().signum() <= 0) {
            throw new IllegalArgumentException(
                    "El salario debe ser mayor que cero."
            );
        }

        if (command.currency() == null
                || !command.currency().matches("[A-Za-z]{3}")) {
            throw new IllegalArgumentException(
                    "currency debe tener código ISO de tres letras, por ejemplo COP."
            );
        }

        if (command.effectiveDate() == null) {
            throw new IllegalArgumentException(
                    "effectiveDate es obligatoria."
            );
        }
    }

    private EmployeeSalaryHistoryItemDto toItemDto(
            EmployeeSalaryHistory history
    ) {
        return new EmployeeSalaryHistoryItemDto(
                history.getId(),
                history.getTenantId(),
                history.getEmployee().getId(),
                history.getSalary(),
                history.getCurrency(),
                history.getEffectiveDate(),
                history.getReason()
        );
    }
}