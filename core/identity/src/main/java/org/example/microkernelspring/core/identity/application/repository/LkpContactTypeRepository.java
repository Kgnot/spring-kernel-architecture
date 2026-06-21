package org.example.microkernelspring.core.identity.application.repository;

import org.example.microkernelspring.core.identity.domain.entity.LkpContactType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LkpContactTypeRepository extends JpaRepository<LkpContactType, UUID> {

    Optional<LkpContactType> findByCode(String code);
}
