package com.springapplication.tracklyapp.config;


import com.springapplication.tracklyapp.config.DevUserSeeder;
import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DevUserSeeder}.
 *
 * This test explicitly stubs Environment properties so the output
 * is deterministic regardless of defaults or external config.
 */
class DevUserSeederTest {

    @Test
    @DisplayName("Creates admin and sample users (idempotent) with explicit emails from Environment")
    void seedsAdminAndUsers_withExplicitEnv() throws Exception {
        // Mocks
        UserRepository userRepo = mock(UserRepository.class);
        RoleRepository roleRepo = mock(RoleRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        Environment env = mock(Environment.class);

        // ---- Stub environment to force deterministic input ----
        when(env.getProperty("trackly.seed.admin.email")).thenReturn("admin@trackly.com");
        when(env.getProperty("trackly.seed.admin.password")).thenReturn("Admin123!");
        when(env.getProperty("trackly.seed.user.emails")).thenReturn("alice@trackly.com,bob@trackly.com");
        when(env.getProperty("trackly.seed.user.password")).thenReturn("User123!");

        // Roles exist
        when(roleRepo.findByName("USER")).thenReturn(Optional.of(new Role("USER", "std")));
        when(roleRepo.findByName("ADMIN")).thenReturn(Optional.of(new Role("ADMIN", "adm")));

        // No existing users on first run
        when(userRepo.findByEmail("admin@trackly.com")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("alice@trackly.com")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("bob@trackly.com")).thenReturn(Optional.empty());

        // Password encoding
        when(encoder.encode("Admin123!")).thenReturn("$2a$admin");
        when(encoder.encode("User123!")).thenReturn("$2a$user");

        // Execute
        DevUserSeeder seeder = new DevUserSeeder(userRepo, roleRepo, encoder, env);
        seeder.run(null);

        // Verify saves (1 admin + 2 users)
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepo, times(3)).save(captor.capture());

        List<User> saved = captor.getAllValues();
        assertThat(saved).extracting(User::getEmail)
                .containsExactlyInAnyOrder("admin@trackly.com", "alice@trackly.com", "bob@trackly.com");

        // Spot-check encoded passwords used (admin and a sample user)
        assertThat(saved.stream().filter(u -> u.getEmail().equals("admin@trackly.com")).findFirst().get().getPasswordHash())
                .isEqualTo("$2a$admin");
        assertThat(saved.stream().filter(u -> u.getEmail().equals("alice@trackly.com")).findFirst().get().getPasswordHash())
                .isEqualTo("$2a$user");

        // ---- Idempotency: second run with same env should NOT save again ----
        reset(userRepo);
        when(userRepo.findByEmail("admin@trackly.com")).thenReturn(Optional.of(new User()));
        when(userRepo.findByEmail("alice@trackly.com")).thenReturn(Optional.of(new User()));
        when(userRepo.findByEmail("bob@trackly.com")).thenReturn(Optional.of(new User()));

        seeder.run(null);
        verify(userRepo, never()).save(any(User.class));
    }
}
