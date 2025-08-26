package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test to ensure USER and ADMIN roles exist after the app context starts.
 * Works whether roles are provided by the Java seeder or by Flyway SQL.
 */
@SpringBootTest
class RoleSeederIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Base roles USER and ADMIN exist")
    void baseRolesExist() {
        assertThat(roleRepository.findByName("USER")).isPresent();
        assertThat(roleRepository.findByName("ADMIN")).isPresent();
    }
}

