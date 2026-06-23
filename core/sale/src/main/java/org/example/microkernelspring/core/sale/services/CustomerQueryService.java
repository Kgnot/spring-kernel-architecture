package org.example.microkernelspring.core.sale.services;


import org.example.microkernelspring.core.sale.persistence.entity.Customer;
import org.example.microkernelspring.core.sale.persistence.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerQueryService {

    private final CustomerRepository customerRepository;

    public List<CustomerDto> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto findById(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // DTO interno del servicio
    @Data
    public static class CustomerDto {
        private UUID id;
        private UUID tenantId;
        private UUID profileId;
        private String companyName;
        private String taxId;
        private BigDecimal creditLimit;
    }

    // Mapper interno del servicio
    public static class CustomerMapper {
        public static CustomerDto toDto(Customer entity) {
            if (entity == null) return null;
            CustomerDto dto = new CustomerDto();
            dto.setId(entity.getId());
            dto.setTenantId(entity.getTenantId());
            dto.setProfileId(entity.getProfileId());
            dto.setCompanyName(entity.getCompanyName());
            dto.setTaxId(entity.getTaxId());
            dto.setCreditLimit(entity.getCreditLimit());
            return dto;
        }
    }
}