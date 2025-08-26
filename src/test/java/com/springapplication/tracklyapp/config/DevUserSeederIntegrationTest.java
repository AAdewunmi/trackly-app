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
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full-context test that verifies dev seeding creates admin + users with roles.
 * Requires the dev profile to activate the seeder.
 */
@SpringBootTest
@ActiveProfiles("test-seed")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // don't replace with H2
@TestPropertySource(properties = {
        // Point to CI Postgres service (matches ci.yml service env)
        "spring.datasource.url=jdbc:postgresql://localhost:5433/trackly",
        "spring.datasource.username=trackly",
        "spring.datasource.password=change-me",
        "spring.jpa.hibernate.ddl-auto=update",

        // Deterministic seed inputs
        "trackly.seed.admin.email=admin@trackly.com",
        "trackly.seed.admin.password=Admin123!",
        "trackly.seed.user.emails=alice@trackly.com,bob@trackly.com",
        "trackly.seed.user.password=User123!"
})
class DevUserSeederIntegrationTest {

    @Autowired private UserRepository users;
    @Autowired private RoleRepository roles;

    @Test
    @DisplayName("Admin and sample users exist with correct roles")
    void seededUsersExist() {
        User admin = users.findByEmail("admin@trackly.com").orElseThrow();
        assertThat(admin.getRoles()).extracting("name").contains("USER", "ADMIN");

        User alice = users.findByEmail("alice@trackly.com").orElseThrow();
        assertThat(alice.getRoles()).extracting("name").containsExactly("USER");
    }
}
