package org.example.microkernelspring.core.tenant.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.Instant;
import java.util.UUID;

/**
 * Sedes/sucursales/bodegas físicas del tenant. La dirección vive aquí mismo
 * (no en tabla address separada) porque es el dato crítico de esta entidad;
 * country/state/city son ids LÓGICOS hacia el schema "geo" (sin FK real
 * entre schemas). stock.inventory se ubica por siteId (referencia lógica).
 * <p>
 * Requiere la dependencia de Hibernate Spatial (org.hibernate:hibernate-spatial)
 * y la extensión PostGIS habilitada en la base de datos para el campo "location".
 */
@Entity
@Table(name = "sites", schema = "tenant")
@Data
public class Site {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    /**
     * Ej: "Sede Norte", "Bodega Principal"
     */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_type_id", nullable = false)
    private LkpSiteType siteType;

    /**
     * Calle/carrera/número, dirección embebida a propósito (no tabla address aparte)
     */
    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    /**
     * Complemento: oficina, piso, referencia
     */
    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    /**
     * Referencia LÓGICA a geo.country.id, sin FK real entre schemas.
     */
    @Column(name = "country_id")
    private UUID countryId;

    /**
     * Referencia LÓGICA a geo.state_province.id.
     */
    @Column(name = "state_province_id")
    private UUID stateProvinceId;

    /**
     * Referencia LÓGICA a geo.city.id.
     */
    @Column(name = "city_id")
    private UUID cityId;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    /**
     * PostGIS: lat/lng de la sede, para geocercas, ruteo y distancia a clientes.
     */
    @Column(name = "location", columnDefinition = "geography(Point,4326)")
    private Point location;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Site() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
