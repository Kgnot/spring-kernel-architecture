package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String email;

    @OneToMany(
            mappedBy = "tenant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<TenantPlugin> plugins = new HashSet<>();
}