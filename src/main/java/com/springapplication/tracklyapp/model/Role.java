package com.springapplication.tracklyapp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an authorization role (e.g., ROLE_USER, ROLE_ADMIN).
 */
@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name = "uq_roles_name", columnNames = "name"))
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID roleId;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Role() {}

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UUID getRoleId() { return roleId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role r)) return false;
        return roleId != null && roleId.equals(r.roleId);
    }
    @Override public int hashCode() { return Objects.hashCode(roleId); }

    @Override public String toString() {
        return "Role{roleId=" + roleId + ", name='" + name + "'}";
    }
}


