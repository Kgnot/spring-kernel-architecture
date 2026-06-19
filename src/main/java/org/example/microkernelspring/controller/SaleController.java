package org.example.microkernelspring.controller;


import org.example.microkernelspring.controller.request.CreateSaleRequest;
import org.example.microkernelspring.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                request
        );

        return ResponseEntity.ok().build();
    }
}