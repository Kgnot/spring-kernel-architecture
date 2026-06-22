package org.example.microkernelspring.core.stock.usecase;

import lombok.RequiredArgsConstructor;
import org.example.microkernelspring.core.stock.entity.LkpProductCategory;
import org.example.microkernelspring.core.stock.entity.LkpUnitOfMeasure;
import org.example.microkernelspring.core.stock.entity.Product;
import org.example.microkernelspring.core.stock.repository.LkpProductCategoryRepository;
import org.example.microkernelspring.core.stock.repository.LkpUnitOfMeasureRepository;
import org.example.microkernelspring.core.stock.repository.ProductRepository;
import org.example.microkernelspring.core.stock.service.dto.ProductDto;
import org.example.microkernelspring.core.stock.service.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final LkpProductCategoryRepository categoryRepository;
    private final LkpUnitOfMeasureRepository unitOfMeasureRepository;

    @Transactional
    public ProductDto execute(ProductDto dto) {
        LkpProductCategory category = dto.categoryId() != null
                ? categoryRepository.findById(dto.categoryId()).orElse(null)
                : null;

        LkpUnitOfMeasure unitOfMeasure = unitOfMeasureRepository
                .findById(dto.unitOfMeasureId())
                .orElseThrow(() -> new RuntimeException("UnitOfMeasure not found"));

        Product entity = ProductMapper.toEntity(dto, category, unitOfMeasure);

        return ProductMapper.toDto(productRepository.save(entity));
    }
}