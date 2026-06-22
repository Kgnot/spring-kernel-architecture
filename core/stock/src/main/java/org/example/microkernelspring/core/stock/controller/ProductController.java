package org.example.microkernelspring.core.stock.controller;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.controller.mapper.ProductWebMapper;
import org.example.microkernelspring.core.stock.controller.request.CreateProductRequest;
import org.example.microkernelspring.core.stock.controller.response.ProductResponse;
import org.example.microkernelspring.core.stock.service.ProductService;
import org.example.microkernelspring.core.stock.usecase.CreateProductUseCase;
import org.example.microkernelspring.core.stock.usecase.DeleteProductUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants/{tenantId}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(@PathVariable UUID tenantId) {
        List<ProductResponse> response = productService.findAllByTenant(tenantId).stream()
                .map(ProductWebMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable UUID tenantId,
            @PathVariable UUID id
    ) {
        ProductResponse response = ProductWebMapper.toResponse(
                productService.findByIdAndTenant(id, tenantId)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @PathVariable UUID tenantId,
            @RequestBody CreateProductRequest request
    ) {
        ProductResponse response = ProductWebMapper.toResponse(
                createProductUseCase.execute(ProductWebMapper.toServiceDto(request))
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID tenantId,
            @PathVariable UUID id
    ) {
        deleteProductUseCase.execute(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}