package org.example.microkernelspring.core.stock.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.entity.Product;
import org.example.microkernelspring.core.stock.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public void execute(UUID productId, UUID tenantId) {
        Product product = productRepository.findByIdAndTenantId(productId, tenantId)
                .orElseThrow(() -> new RuntimeException("Product not found or access denied"));

        productRepository.delete(product);
    }
}