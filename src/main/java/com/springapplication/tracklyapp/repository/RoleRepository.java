package com.springapplication.tracklyapp.repository;

import com.springapplication.tracklyapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for {@link Role}.
 * <p>
 * Used to resolve canonical roles (e.g., ROLE_USER, ROLE_ADMIN) at runtime.
 */
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Find a role by its canonical name (e.g., ROLE_USER).
     * @param name role name
     * @return optional role
     */
    Optional<Role> findByName(String name);

    /**
     * Check whether a role exists with the given name.
     * @param name role name
     * @return true if present
     */
    boolean existsByName(String name);
}



