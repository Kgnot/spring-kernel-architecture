package org.example.microkernelspring.core.sale.application.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.repository.LkpPaymentMethodRepository;
import org.example.microkernelspring.core.sale.domain.entity.LkpPaymentMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LkpPaymentMethodQueryService {

    private final LkpPaymentMethodRepository repository;

    public List<PaymentMethodDto> findAll() {
        return repository.findAll().stream()
                .map(PaymentMethodMapper::toDto)
                .collect(Collectors.toList());
    }

    @Data
    public static class PaymentMethodDto {
        private UUID id;
        private String code;
        private String name;
    }

    public static class PaymentMethodMapper {
        public static PaymentMethodDto toDto(LkpPaymentMethod entity) {
            if (entity == null) return null;
            PaymentMethodDto dto = new PaymentMethodDto();
            dto.setId(entity.getId());
            dto.setCode(entity.getCode());
            dto.setName(entity.getName());
            return dto;
        }
    }
}