package org.example.microkernelspring.core.sale.services;

import org.example.microkernelspring.core.sale.persistence.entity.Invoice;
import org.example.microkernelspring.core.sale.persistence.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;

    public List<InvoiceDto> findAll() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public InvoiceDto findById(UUID id) {
        return invoiceRepository.findById(id)
                .map(InvoiceMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    // DTO interno del servicio
    @Data
    public static class InvoiceDto {
        private UUID id;
        private UUID tenantId;
        private UUID siteId;
        private UUID customerId;
        private UUID employeeId;
        private String invoiceNumber;
        private UUID statusId;
        private BigDecimal subtotal;
        private BigDecimal taxTotal;
        private BigDecimal discountTotal;
        private BigDecimal grandTotal;
        private String currency;
        private Instant issuedAt;
        private Instant dueAt;
    }

    // Mapper interno del servicio
    public static class InvoiceMapper {
        public static InvoiceDto toDto(Invoice entity) {
            if (entity == null) return null;
            InvoiceDto dto = new InvoiceDto();
            dto.setId(entity.getId());
            dto.setTenantId(entity.getTenantId());
            dto.setSiteId(entity.getSiteId());
            dto.setCustomerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null);
            dto.setEmployeeId(entity.getEmployeeId());
            dto.setInvoiceNumber(entity.getInvoiceNumber());
            dto.setStatusId(entity.getStatus() != null ? entity.getStatus().getId() : null);
            dto.setSubtotal(entity.getSubtotal());
            dto.setTaxTotal(entity.getTaxTotal());
            dto.setDiscountTotal(entity.getDiscountTotal());
            dto.setGrandTotal(entity.getGrandTotal());
            dto.setCurrency(entity.getCurrency());
            dto.setIssuedAt(entity.getIssuedAt());
            dto.setDueAt(entity.getDueAt());
            return dto;
        }
    }
}