package com.springapplication.tracklyapp.service;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Custom {@link UserDetailsService} that authenticates users by their email address.
 *
 * <p>This service queries the application's {@link UserRepository} for a {@link User}
 * matching the given email (Spring Security supplies the "username" form field, here used
 * as an email). The user's roles are translated to Spring Security authorities using the
 * {@code ROLE_} convention expected by the framework.
 *
 * <p>Example mapping:
 * <ul>
 *   <li>Role "USER" -> GrantedAuthority "ROLE_USER"</li>
 *   <li>Role "ROLE_ADMIN" (already prefixed) -> GrantedAuthority "ROLE_ADMIN"</li>
 * </ul>
 *
 * <p>The resulting {@link UserDetails} instance contains:
 * <ul>
 *   <li>username: the user's email</li>
 *   <li>password: the user's BCrypt hash</li>
 *   <li>authorities: derived from user roles</li>
 * </ul>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user from the database by email. The {@code username} parameter supplied by
     * Spring Security is treated as an email address in this application.
     *
     * @param email the email address submitted via the login form's "username" field
     * @return a Spring Security {@link UserDetails} representing the authenticated user
     * @throws UsernameNotFoundException if no user is found for the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final String lookup = Optional.ofNullable(email)
                .map(String::trim)
                .orElse("");

        User user = userRepository.findByEmail(lookup)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + lookup));

        Collection<GrantedAuthority> authorities = toAuthorities(user);

        // Using Spring's built-in User implementation for simplicity.
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Convert the domain user's roles to Spring Security {@link GrantedAuthority}s.
     * Ensures each role name is prefixed with {@code ROLE_} exactly once.
     */
    private Collection<GrantedAuthority> toAuthorities(User user) {
        if (user.getRoles() == null) {
            return java.util.List.of();
        }
        return user.getRoles().stream()
                .filter(Objects::nonNull)
                .map(Role::getName)
                .filter(Objects::nonNull)
                .map(this::ensureRolePrefix)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    /** Prefix role name with {@code ROLE_} if it is not already prefixed. */
    private String ensureRolePrefix(String name) {
        final String normalized = name.trim();
        return normalized.startsWith("ROLE_") ? normalized : "ROLE_" + normalized;
    }
}

