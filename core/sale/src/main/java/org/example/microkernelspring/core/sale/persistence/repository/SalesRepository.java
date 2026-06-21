package org.example.microkernelspring.core.sale.persistence.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class SalesRepository {

    public BigDecimal getPreviousMonthTotal(UUID tenantId) {

        return BigDecimal.valueOf(
                1000 + 500
        );
    }
}
