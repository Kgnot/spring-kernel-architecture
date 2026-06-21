package org.example.microkernelspring.core.identity.application.repository;

import org.example.microkernelspring.core.identity.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByTenantIdIsNullAndName(String name);
}
