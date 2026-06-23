package org.example.microkernelspring.core.sale.controllers;

import org.example.microkernelspring.core.sale.api.SaleApi;
import org.example.microkernelspring.core.sale.api.SaleApi.InvoiceApi;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final SaleApi saleApi;

    @PostMapping
    public void create(@RequestBody InvoiceApi invoice) {
        saleApi.createInvoice(invoice);
    }
}