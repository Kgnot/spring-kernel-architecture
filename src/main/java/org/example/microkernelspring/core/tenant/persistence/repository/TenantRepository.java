package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Optional<Tenant> findByEmail(String email);
}