package org.example.microkernelspring.core.geo.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Catálogo global de países. Cualquier schema que necesite país
 * referencia esto por id de forma LÓGICA (sin FK real entre schemas).
 */
@Entity
@Table(
        name = "country",
        schema = "geo",
        uniqueConstraints = @UniqueConstraint(columnNames = "iso_code")
)
public class Country {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** ISO 3166-1 alpha-2, ej: CO, MX, US */
    @Column(name = "iso_code", nullable = false, length = 2)
    private String isoCode;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** ej: +57 */
    @Column(name = "phone_prefix", length = 6)
    private String phonePrefix;

    public Country() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }
}
