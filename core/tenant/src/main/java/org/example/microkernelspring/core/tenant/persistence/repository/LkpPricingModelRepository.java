package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpPricingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository de LkpPricingModel. Catálogo pequeño y estático, sin paginación. */
public interface LkpPricingModelRepository extends JpaRepository<LkpPricingModel, UUID> {

    Optional<LkpPricingModel> findByCode(String code);

    List<LkpPricingModel> findAllByOrderByName();
}
