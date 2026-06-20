package org.example.microkernelspring.core.sale.persistence.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class SalesRepository {

    public BigDecimal getPreviousMonthTotal(Long tenantId) {

        return BigDecimal.valueOf(
                tenantId * 1000 + 500
        );
    }
}
