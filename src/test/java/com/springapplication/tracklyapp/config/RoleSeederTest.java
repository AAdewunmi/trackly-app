package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RoleSeeder}.
 */
class RoleSeederTest {

    @Test
    @DisplayName("Seeds both USER and ADMIN when none exist")
    void seedsWhenMissing() throws Exception {
        RoleRepository repo = mock(RoleRepository.class);
        when(repo.existsByName("USER")).thenReturn(false);
        when(repo.existsByName("ADMIN")).thenReturn(false);

        RoleSeeder seeder = new RoleSeeder(repo);
        seeder.run(null);

        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        verify(repo, times(2)).save(captor.capture());

        assertThat(captor.getAllValues())
                .extracting(Role::getName)
                .containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    @DisplayName("Does not create duplicates when roles already exist")
    void idempotentWhenPresent() throws Exception {
        RoleRepository repo = mock(RoleRepository.class);
        when(repo.existsByName("USER")).thenReturn(true);
        when(repo.existsByName("ADMIN")).thenReturn(true);

        RoleSeeder seeder = new RoleSeeder(repo);
        seeder.run(null);

        verify(repo, never()).save(any(Role.class));
    }
}

