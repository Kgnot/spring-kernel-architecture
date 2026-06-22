package org.example.microkernelspring.core.hr.service;

import org.example.microkernelspring.core.hr.entity.Employee;
import org.example.microkernelspring.core.hr.port.IdentityHrPort;
import org.example.microkernelspring.core.hr.repository.EmployeeRepository;
import org.example.microkernelspring.core.hr.service.dto.EmployeeListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IdentityHrPort identityHrPort;

    public EmployeeService(EmployeeRepository employeeRepository, IdentityHrPort identityHrPort) {
        this.employeeRepository = employeeRepository;
        this.identityHrPort = identityHrPort;
    }

    public Page<EmployeeListItemDto> findByTenant(UUID tenantId, Pageable pageable) {
        return employeeRepository.findByTenantId(tenantId, pageable).map(this::toListItemDto);
    }

    public Employee findEntityByIdAndTenant(UUID employeeId, UUID tenantId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado."));

        if (!employee.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("El empleado no pertenece al tenant indicado.");
        }

        return employee;
    }

    private EmployeeListItemDto toListItemDto(Employee employee) {
        var profileDetails = identityHrPort.getProfileDetails(employee.getProfileId());
        if (profileDetails.isEmpty()) {
            throw new IllegalStateException("No se encontraron los detalles del perfil para el empleado con ID: " + employee.getId());
        }
        return new EmployeeListItemDto(
                employee.getId(),
                employee.getTenantId(),
                profileDetails.get().firstName(),
                profileDetails.get().lastName(),
                employee.getEmploymentStatus().getCode()
        );
    }
}