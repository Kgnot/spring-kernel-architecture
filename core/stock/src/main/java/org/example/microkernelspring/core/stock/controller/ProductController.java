package org.example.microkernelspring.core.stock.controller;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.ProductWebMapper;
import org.example.microkernelspring.core.stock.controller.request.CreateProductRequest;
import org.example.microkernelspring.core.stock.controller.response.ProductResponse;
import org.example.microkernelspring.core.stock.service.ProductService;
import org.example.microkernelspring.core.stock.usecase.CreateProductUseCase;
import org.example.microkernelspring.core.stock.usecase.DeleteProductUseCase;
import org.example.microkernelspring.shared.infra.util.SecurityContextHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        List<ProductResponse> response = productService.findAllByTenant(tenantId).stream()
                .map(ProductWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        ProductResponse response = ProductWebMapper.toResponse(
                productService.findByIdAndTenant(id, tenantId)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody CreateProductRequest request) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        ProductResponse response = ProductWebMapper.toResponse(
                createProductUseCase.execute(ProductWebMapper.toServiceDto(request, tenantId))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID tenantId = SecurityContextHelper.getCurrentTenantId();

        deleteProductUseCase.execute(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}