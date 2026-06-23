package org.example.microkernelspring.core.sale.application.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.repository.LkpCustomerTypeRepository;
import org.example.microkernelspring.core.sale.domain.entity.LkpCustomerType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LkpCustomerTypeQueryService {

    private final LkpCustomerTypeRepository repository;

    public List<CustomerTypeDto> findAll() {
        return repository.findAll().stream()
                .map(CustomerTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Data
    public static class CustomerTypeDto {
        private UUID id;
        private String code;
        private String name;
    }

    public static class CustomerTypeMapper {
        public static CustomerTypeDto toDto(LkpCustomerType entity) {
            if (entity == null) return null;
            CustomerTypeDto dto = new CustomerTypeDto();
            dto.setId(entity.getId());
            dto.setCode(entity.getCode());
            dto.setName(entity.getName());
            return dto;
        }
    }
}