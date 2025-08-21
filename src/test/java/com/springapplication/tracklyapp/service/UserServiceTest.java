package com.springapplication.tracklyapp.service;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private static final int ENCODER_STRENGTH = 10;

    private UserRepository users;
    private RoleRepository roles;
    private PasswordEncoder encoder;
    private UserService service;

    @BeforeEach
    void setUp() {
        users = mock(UserRepository.class);
        roles = mock(RoleRepository.class);
        encoder = new BCryptPasswordEncoder(ENCODER_STRENGTH);
        service = new UserService(users, roles, encoder);
    }

    @Test
    void register_hashesPassword_and_assignsRoleUser() {
        when(users.existsByEmail("alice@example.com")).thenReturn(false);
        when(roles.findByName("ROLE_USER")).thenReturn(Optional.of(new Role("ROLE_USER", "default")));
        when(users.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User u = service.register("Alice", "alice@example.com", "Password123");

        assertThat(u.getPasswordHash()).isNotEqualTo("Password123");
        assertThat(encoder.matches("Password123", u.getPasswordHash())).isTrue();
        assertThat(u.getRoles()).extracting(Role::getName).contains("ROLE_USER");
    }

    @Test
    void register_rejectsDuplicateEmail() {
        when(users.existsByEmail("bob@example.com")).thenReturn(true);
        assertThatThrownBy(() -> service.register("Bob", "bob@example.com", "Password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    void register_rejectsWeakPassword() {
        when(users.existsByEmail("weak@example.com")).thenReturn(false);
        assertThatThrownBy(() -> service.register("Weak", "weak@example.com", "weak"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password must be at least 8 chars");
    }

    @Test
    void register_throwsWhenRoleNotFound() {
        when(users.existsByEmail("charlie@example.com")).thenReturn(false);
        when(roles.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.register("Charlie", "charlie@example.com", "Password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Role not found");
    }
}