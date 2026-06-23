package org.example.microkernelspring.core.sale.application.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.sale.application.repository.InvoiceRepository;
import org.example.microkernelspring.core.sale.domain.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;

    public Page<InvoiceDto> findAll(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
                .map(InvoiceMapper::toDto);
    }

    public Page<InvoiceDto> findByTenantId(UUID tenantId, Pageable pageable) {
        return invoiceRepository.findByTenantId(tenantId, pageable)
                .map(InvoiceMapper::toDto);
    }

    public Page<InvoiceDto> findByCustomerId(UUID customerId, Pageable pageable) {
        return invoiceRepository.findByCustomerId(customerId, pageable)
                .map(InvoiceMapper::toDto);
    }

    public InvoiceDto findById(UUID id) {
        return invoiceRepository.findById(id)
                .map(InvoiceMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

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
