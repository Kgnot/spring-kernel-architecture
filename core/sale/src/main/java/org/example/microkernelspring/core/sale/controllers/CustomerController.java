package org.example.microkernelspring.core.sale.controllers;

import org.example.microkernelspring.core.sale.api.SaleApi;
import org.example.microkernelspring.core.sale.api.SaleApi.CustomerApi;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final SaleApi saleApi;

    @GetMapping
    public List<CustomerApi> getAll() {
        return saleApi.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerApi getById(@PathVariable UUID id) {
        return saleApi.getCustomerById(id);
    }

    @PostMapping
    public void create(@RequestBody CustomerApi customer) {
        saleApi.createCustomer(customer);
    }
}