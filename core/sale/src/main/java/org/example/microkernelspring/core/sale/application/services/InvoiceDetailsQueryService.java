package org.example.microkernelspring.core.sale.application.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.microkernelspring.core.sale.application.repository.InvoiceDetailsRepository;
import org.example.microkernelspring.core.sale.domain.entity.InvoiceDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceDetailsQueryService {

    private final InvoiceDetailsRepository repository;

    public Page<InvoiceDetailDto> findByInvoiceId(UUID invoiceId, Pageable pageable) {
        if (pageable == null) {
            //TODO
            log.info("pageable is null +- se debe arreglar");
            throw new IllegalArgumentException("Pageable must not be null");
        }
        return repository.findByInvoiceId(invoiceId, pageable)
                .map(InvoiceDetailsMapper::toDto);
    }

    @Data
    public static class InvoiceDetailDto {
        private UUID id;
        private UUID invoiceId;
        private UUID productId;
        private UUID serviceId;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal discount;
        private BigDecimal taxRate;
        private BigDecimal lineTotal;
    }

    public static class InvoiceDetailsMapper {
        public static InvoiceDetailDto toDto(InvoiceDetails entity) {
            if (entity == null) return null;
            InvoiceDetailDto dto = new InvoiceDetailDto();
            dto.setId(entity.getId());
            dto.setInvoiceId(entity.getInvoice().getId());
            dto.setProductId(entity.getProductId());
            dto.setServiceId(entity.getService() != null ? entity.getService().getId() : null);
            dto.setDescription(entity.getDescription());
            dto.setQuantity(entity.getQuantity());
            dto.setUnitPrice(entity.getUnitPrice());
            dto.setDiscount(entity.getDiscount());
            dto.setTaxRate(entity.getTaxRate());
            dto.setLineTotal(entity.getLineTotal());
            return dto;
        }
    }
}
