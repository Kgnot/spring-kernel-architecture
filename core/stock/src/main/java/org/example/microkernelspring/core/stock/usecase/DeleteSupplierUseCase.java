package org.example.microkernelspring.core.stock.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.entity.Supplier;
import org.example.microkernelspring.core.stock.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteSupplierUseCase {

    private final SupplierRepository supplierRepository;

    @Transactional
    public void execute(UUID supplierId, UUID tenantId) {
        Supplier supplier = supplierRepository.findByIdAndTenantId(supplierId, tenantId)
                .orElseThrow(() -> new RuntimeException("Supplier not found or access denied"));

        supplierRepository.delete(supplier);
    }
}