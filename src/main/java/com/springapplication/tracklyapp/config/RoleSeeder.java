package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ensures canonical roles exist on startup.
 */
@Configuration
public class RoleSeeder {

    @Bean
    CommandLineRunner seedRoles(RoleRepository roles) {
        return args -> {
            if (!roles.existsByName("ROLE_USER")) {
                roles.save(new Role("ROLE_USER", "Default application user role"));
            }
            if (!roles.existsByName("ROLE_ADMIN")) {
                roles.save(new Role("ROLE_ADMIN", "Administrator role"));
            }
        };
    }
}

