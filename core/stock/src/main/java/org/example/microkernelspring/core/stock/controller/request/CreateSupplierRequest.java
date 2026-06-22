package org.example.microkernelspring.core.stock.controller.request;


public record CreateSupplierRequest(
        String name,
        String taxId,
        boolean active
) {}
