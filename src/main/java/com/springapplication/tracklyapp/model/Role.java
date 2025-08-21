package com.springapplication.tracklyapp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an authorization role (e.g., ROLE_USER, ROLE_ADMIN).
 * <p>
 * Suggested fields:
 * <ul>
 *   <li><b>id</b> – UUID primary key</li>
 *   <li><b>name</b> – stable identifier for the role (stored as STRING enum)</li>
 *   <li><b>description</b> – human-friendly role description</li>
 *   <li><b>createdAt</b>, <b>updatedAt</b> – audit timestamps</li>
 * </ul>
 */
@Entity
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_roles_name", columnNames = "name")
        }
)
public class Role {

    /**
     * System-generated primary key for the role (UUID).
     */
    @Id
    @GeneratedValue
    @Column(name = "role_id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID id;

    /**
     * Stable role name (e.g., ROLE_USER, ROLE_ADMIN).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 64)
    private RoleName name;

    /**
     * Optional human-readable description of what this role grants.
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * Timestamp when the role record was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    /**
     * Timestamp when the role record was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // --- Constructors ---
    protected Role() {
        // JPA
    }

    public Role(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }

    // --- Getters/Setters ---

    public UUID getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    // --- Equality: based on persistent id ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // --- toString ---

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

    /**
     * Canonical role names.
     */
    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }
}

