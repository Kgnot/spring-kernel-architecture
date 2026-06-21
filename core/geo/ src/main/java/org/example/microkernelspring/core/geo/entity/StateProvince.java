package org.example.microkernelspring.core.geo.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Departamento/estado/provincia, depende del país. FK real porque Country
 * vive en el mismo schema geo.
 */
@Entity
@Table(name = "state_province", schema = "geo")
public class StateProvince {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** ej: código DIVIPOLA en Colombia, o abreviación de estado */
    @Column(name = "code", length = 10)
    private String code;

    public StateProvince() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
