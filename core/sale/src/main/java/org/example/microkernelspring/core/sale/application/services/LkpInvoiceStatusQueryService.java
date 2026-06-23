package org.example.microkernelspring.core.sale.application.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.repository.LkpInvoiceStatusRepository;
import org.example.microkernelspring.core.sale.domain.entity.LkpInvoiceStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LkpInvoiceStatusQueryService {

    private final LkpInvoiceStatusRepository repository;

    public List<InvoiceStatusDto> findAll() {
        return repository.findAll().stream()
                .map(InvoiceStatusMapper::toDto)
                .collect(Collectors.toList());
    }

    @Data
    public static class InvoiceStatusDto {
        private UUID id;
        private String code;
        private String name;
    }

    public static class InvoiceStatusMapper {
        public static InvoiceStatusDto toDto(LkpInvoiceStatus entity) {
            if (entity == null) return null;
            InvoiceStatusDto dto = new InvoiceStatusDto();
            dto.setId(entity.getId());
            dto.setCode(entity.getCode());
            dto.setName(entity.getName());
            return dto;
        }
    }
}