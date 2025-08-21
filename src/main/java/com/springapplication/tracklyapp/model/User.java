package com.springapplication.tracklyapp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.*;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Application user mapped to existing "users" table (user_id, full_name, email, password_hash, created_at, updated_at).
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "users_email_key", columnNames = "email"))
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"),
            uniqueConstraints = @UniqueConstraint(name = "uq_user_roles_user_role", columnNames = {"user_id", "role_id"})
    )
    private Collection<Role> roles = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public User() {}

    public User(String fullName, String email, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User(UUID userId, String fullName, String email, String passwordHash) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public UUID getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public User setFullName(String fullName) { this.fullName = fullName; return this; }
    public String getEmail() { return email; }
    public User setEmail(String email) { this.email = email; return this; }
    public String getPasswordHash() { return passwordHash; }
    public User setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }

    public Collection<Role> getRoles() { return roles; }
    public void setRoles(Collection<Role> roles) { this.roles = roles; }
    public User addRole(Role role) { this.roles.add(role); return this; }
    public User removeRole(Role role) { this.roles.remove(role); return this; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;
        return userId != null && userId.equals(u.userId);
    }
    @Override public int hashCode() { return Objects.hashCode(userId); }

    @Override public String toString() {
        return "User{userId=" + userId + ", email='" + email + "'}";
    }
}