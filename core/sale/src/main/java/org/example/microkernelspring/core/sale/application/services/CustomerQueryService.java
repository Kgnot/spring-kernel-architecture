package org.example.microkernelspring.core.sale.application.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.repository.CustomerRepository;
import org.example.microkernelspring.core.sale.domain.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerQueryService {

    private final CustomerRepository customerRepository;

    public Page<CustomerDto> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(CustomerMapper::toDto);
    }

    public Page<CustomerDto> findByTenantId(UUID tenantId, Pageable pageable) {
        return customerRepository.findByTenantId(tenantId, pageable)
                .map(CustomerMapper::toDto);
    }

    public CustomerDto findById(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Data
    public static class CustomerDto {
        private UUID id;
        private UUID tenantId;
        private UUID profileId;
        private String companyName;
        private String taxId;
        private BigDecimal creditLimit;
    }

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
