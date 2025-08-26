package com.springapplication.tracklyapp.service;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CustomUserDetailsService}.
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Test
    @DisplayName("loadUserByUsername returns UserDetails with normalized ROLE_ authorities")
    void loadUser_success_mapsRoles() {
        // Arrange
        UserRepository repo = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(repo);

        User user = new User(
                UUID.randomUUID(),
                "Alice Johnson",
                "alice@example.com",
                "$2a$10$hashedPassword"
        );
        // Mix of prefixed and unprefixed role names to test normalization
        user.addRole(new Role("USER", "Standard user"));
        user.addRole(new Role("ROLE_ADMIN", "Administrator"));

        when(repo.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails details = service.loadUserByUsername("alice@example.com");

        // Assert
        assertEquals("alice@example.com", details.getUsername());
        assertEquals("$2a$10$hashedPassword", details.getPassword());

        Set<String> authorities = details.getAuthorities()
                .stream().map(a -> a.getAuthority()).collect(Collectors.toSet());

        assertTrue(authorities.contains("ROLE_USER"), "Expected ROLE_USER authority");
        assertTrue(authorities.contains("ROLE_ADMIN"), "Expected ROLE_ADMIN authority");
        assertEquals(2, authorities.size(), "No duplicate/extra authorities expected");

        verify(repo, times(1)).findByEmail("alice@example.com");
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("loadUserByUsername throws UsernameNotFoundException when user missing")
    void loadUser_notFound_throws() {
        // Arrange
        UserRepository repo = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(repo);

        when(repo.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@example.com"));

        verify(repo, times(1)).findByEmail("missing@example.com");
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("loadUserByUsername handles null/empty roles safely")
    void loadUser_nullRoles_ok() {
        // Arrange
        UserRepository repo = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(repo);

        User user = new User(
                UUID.randomUUID(),
                "Bob Smith",
                "bob@example.com",
                "$2a$10$hashedPassword"
        );
        // No roles added (collection exists but empty)
        when(repo.findByEmail("bob@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails details = service.loadUserByUsername("bob@example.com");

        // Assert
        assertEquals("bob@example.com", details.getUsername());
        assertTrue(details.getAuthorities().isEmpty(), "Expected no authorities when user has no roles");
    }

}
