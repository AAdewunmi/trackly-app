package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Ensures base roles exist in the database at application startup.
 *
 * <p>Convention: store role names WITHOUT the {@code ROLE_} prefix, e.g., "USER", "ADMIN".
 * Spring Security mapping will add {@code ROLE_} as needed in {@code CustomUserDetailsService}.
 *
 * <p>Idempotent by design: re-running does not create duplicates due to unique constraint on role name.
 */
@Component
@Profile({"dev", "test-seed","ci"})
@Order(10) // run early, before other bootstrappers that might rely on roles
public class RoleSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RoleSeeder.class);

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Role> required = List.of(
                new Role("USER", "Standard user role"),
                new Role("ADMIN", "Administrator role")
        );

        required.forEach(role -> {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
                log.info("Seeded role: {}", role.getName());
            } else {
                log.debug("Role already present: {}", role.getName());
            }
        });
    }
}
