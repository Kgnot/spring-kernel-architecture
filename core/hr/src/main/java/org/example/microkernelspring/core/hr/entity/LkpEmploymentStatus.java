package org.example.microkernelspring.core.hr.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Estados laborales del empleado (active, on_leave, terminated).
 */
@Entity
@Table(
        name = "lkp_employment_status",
        schema = "hr",
        uniqueConstraints = @UniqueConstraint(columnNames = "code")
)
public class LkpEmploymentStatus {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /** active, on_leave, terminated */
    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public LkpEmploymentStatus() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
