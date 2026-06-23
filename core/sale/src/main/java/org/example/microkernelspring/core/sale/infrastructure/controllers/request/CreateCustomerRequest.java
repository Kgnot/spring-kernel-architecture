package org.example.microkernelspring.core.sale.infrastructure.controllers.request;


import java.math.BigDecimal;
import java.util.UUID;

public record CreateCustomerRequest(
         UUID profileId,
         UUID customerTypeId,
         String companyName,
         String taxId,
         BigDecimal
         creditLimit) {
}