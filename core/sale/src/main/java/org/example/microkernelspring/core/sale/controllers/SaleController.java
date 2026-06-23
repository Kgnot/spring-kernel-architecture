package org.example.microkernelspring.core.sale.controllers;


import org.example.microkernelspring.core.sale.controllers.request.CreateSaleRequest;
import org.example.microkernelspring.core.sale.services.SaleService;
import org.example.microkernelspring.core.sale.services.dto.CreateSaleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(
            SaleService saleService
    ) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Void> createSale(
            @RequestBody CreateSaleRequest request
    ) {

        saleService.createSale(
                request.tenantId(),
                new CreateSaleDto(BigDecimal.valueOf(Long.parseLong(request.amount())))
        );

        return ResponseEntity.ok().build();
    }
}