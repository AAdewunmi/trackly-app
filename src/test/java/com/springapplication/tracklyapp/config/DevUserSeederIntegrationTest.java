package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full-context test that verifies dev seeding creates admin + users with roles.
 * Requires the dev profile to activate the seeder.
 */
@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DevUserSeederIntegrationTest {

    @Autowired private UserRepository users;
    @Autowired private RoleRepository roles;

    @Test
    @DisplayName("Admin and sample users exist with correct roles")
    void seededUsersExist() {
        User admin = users.findByEmail("admin@trackly.local").orElseThrow();
        assertThat(admin.getRoles()).extracting("name").contains("USER", "ADMIN");

        User alice = users.findByEmail("alice@trackly.local").orElseThrow();
        assertThat(alice.getRoles()).extracting("name").containsExactly("USER");
    }
}

