package org.example.microkernelspring.core.identity.application.repository;

import org.example.microkernelspring.core.identity.domain.entity.UsersLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UsersLoginRepository extends JpaRepository<UsersLogin, UUID> {

    Optional<UsersLogin> findByTenantIdAndEmail(UUID tenantId, String email);

    boolean existsByTenantIdAndEmail(UUID tenantId, String email);
}
