package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Entity
@Table(
        name = "lkp_industry",
        schema = "tenant",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
@Data
public class LkpIndustry {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    public LkpIndustry() {
    }
}
