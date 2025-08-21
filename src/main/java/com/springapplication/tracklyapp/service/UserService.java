package com.springapplication.tracklyapp.service;


import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application user service.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Register new users with input validation</li>
 *   <li>Normalize and de-duplicate email addresses</li>
 *   <li>Hash passwords with BCrypt</li>
 *   <li>Auto-assign {@code ROLE_USER} to all new accounts</li>
 * </ul>
 */
@Service
public class UserService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user and auto-assign {@code ROLE_USER}.
     *
     * @param fullName   user's full name (required, 1..100 chars)
     * @param email      unique email (case-insensitive, trimmed)
     * @param rawPassword plaintext password (will be BCrypt-hashed)
     * @return persisted {@link User}
     * @throws IllegalArgumentException if validation fails or email already exists
     * @throws IllegalStateException if canonical ROLE_USER is missing in DB
     */
    @Transactional
    public User register(String fullName, String email, String rawPassword) {
        // Basic validation (you may replace with javax validation in a DTO)
        if (fullName == null || fullName.trim().isEmpty() || fullName.trim().length() > 100) {
            throw new IllegalArgumentException("Full name is required and must be <= 100 characters.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        final String normalizedEmail = email.trim().toLowerCase();

        if (users.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered: " + normalizedEmail);
        }
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        // Resolve canonical ROLE_USER (seeded via migration)
        Role defaultRole = roles.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found. Ensure migrations seeded roles."));

        // Create and persist user
        String passwordHash = passwordEncoder.encode(rawPassword);
        User user = new User(fullName.trim(), normalizedEmail, passwordHash);
        user.addRole(defaultRole);

        return users.save(user);
    }
}
