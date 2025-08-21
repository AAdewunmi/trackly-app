package com.springapplication.tracklyapp.repository;

import com.springapplication.tracklyapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for {@link User}.
 * <p>
 * Provides convenient lookups for authentication and registration flows.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a user by unique email.
     * @param email normalized email address
     * @return optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Check whether a user already exists with the given email.
     * @param email normalized email
     * @return true if present
     */
    boolean existsByEmail(String email);
}


