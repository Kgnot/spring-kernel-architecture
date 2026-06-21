package org.example.microkernelspring.core.tenant.persistence.repository;

import org.example.microkernelspring.core.tenant.persistence.entity.LkpRelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Repository de LkpRelationshipType. Catálogo pequeño y estático, sin paginación. */
public interface LkpRelationshipTypeRepository extends JpaRepository<LkpRelationshipType, UUID> {

    Optional<LkpRelationshipType> findByCode(String code);

    List<LkpRelationshipType> findAllByOrderByName();
}
