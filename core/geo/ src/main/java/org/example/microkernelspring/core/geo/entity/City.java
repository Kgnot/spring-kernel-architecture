package org.example.microkernelspring.core.geo.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Ciudad/municipio. Resto de schemas (tenant, sale, identity) guardan
 * cityId como referencia LÓGICA a esta tabla, no como FK real.
 */
@Entity
@Table(name = "city", schema = "geo")
public class City {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "state_province_id", nullable = false)
    private StateProvince stateProvince;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    public City() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public StateProvince getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(StateProvince stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
