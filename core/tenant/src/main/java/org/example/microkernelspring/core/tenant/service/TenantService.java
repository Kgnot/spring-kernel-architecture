package org.example.microkernelspring.core.tenant.service;

import org.example.microkernelspring.core.tenant.persistence.repository.TenantRepository;
import org.example.microkernelspring.core.tenant.service.dto.TenantDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantService {

    private final TenantRepository repository;

    public TenantService(TenantRepository repository) {
        this.repository = repository;
    }

    public Optional<TenantDto> findBySubdomain(String subdomain) {
        return repository.findBySubdomain(subdomain)
                .map(tenant ->
                        new TenantDto(
                                tenant.getId(),
                                tenant.getLegalName(),
                                tenant.getTradeName(),
                                tenant.getTaxId(),
                                tenant.getIndustry().getName(),
                                tenant.getStatus().getName(),
                                tenant.getSubdomain(),
                                tenant.getTimezone(),
                                tenant.getDefaultCurrency(),
                                tenant.getTrialEndsAt(),
                                tenant.getCreatedAt(),
                                tenant.getUpdatedAt(),
                                tenant.getDeletedAt()
                        ));
    }

}
