package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Dev-only bootstrap that ensures a default ADMIN user (also has USER role)
 * and a few regular USER accounts exist with BCrypted passwords.
 *
 * <p>Idempotent: will not duplicate users on re-run. Uses repository lookups by email.
 *
 * <p>Credentials are configurable via properties or environment variables. See defaults below.
 * Never enable this in production.
 */
@Component
@Profile({"dev", "test-seed", "ci"})
@Order(20)
public class DevUserSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevUserSeeder.class);

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    // Defaults (can be overridden by properties)
    private final String adminEmail;
    private final String adminPassword;
    private final List<String> sampleUserEmails;
    private final String sampleUserPassword;

    public DevUserSeeder(
            UserRepository users,
            RoleRepository roles,
            PasswordEncoder passwordEncoder,
            // Resolve from application-dev.properties or env; fall back to defaults if missing
            org.springframework.core.env.Environment env
    ) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;

        this.adminEmail = Optional.ofNullable(env.getProperty("trackly.seed.admin.email")).orElse("admin@trackly.com");
        this.adminPassword = Optional.ofNullable(env.getProperty("trackly.seed.admin.password")).orElse("Admin123!");
        this.sampleUserPassword = Optional.ofNullable(env.getProperty("trackly.seed.user.password")).orElse("User123!");
        // Comma-separated list supported: trackly.seed.user.emails=user1@trackly.local,user2@trackly.local
        String csv = Optional.ofNullable(env.getProperty("trackly.seed.user.emails"))
                .orElse("alisha@trackly.com,billy@trackly.com");
        this.sampleUserEmails = java.util.Arrays.stream(csv.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    @Override
    public void run(ApplicationArguments args) {
        Role userRole = roles.findByName("USER").orElseThrow(() ->
                new IllegalStateException("Missing base role USER. Ensure RoleSeeder or migration runs first."));
        Role adminRole = roles.findByName("ADMIN").orElseThrow(() ->
                new IllegalStateException("Missing base role ADMIN. Ensure RoleSeeder or migration runs first."));

        // 1) Admin
        users.findByEmail(adminEmail).ifPresentOrElse(
                u -> log.debug("Admin user already present: {}", adminEmail),
                () -> {
                    User admin = new User("Trackly Admin", adminEmail, passwordEncoder.encode(adminPassword));
                    admin.addRole(userRole).addRole(adminRole);
                    users.save(admin);
                    log.info("Seeded DEV admin user: {}", adminEmail);
                });

        // 2) Sample users
        for (String email : sampleUserEmails) {
            users.findByEmail(email).ifPresentOrElse(
                    u -> log.debug("Sample user already present: {}", email),
                    () -> {
                        String display = email.substring(0, email.indexOf('@'));
                        String fullName = display.substring(0, 1).toUpperCase() + display.substring(1).replace('.', ' ');
                        User u = new User(fullName, email, passwordEncoder.encode(sampleUserPassword));
                        u.addRole(userRole);
                        users.save(u);
                        log.info("Seeded DEV user: {}", email);
                    });
        }
    }
}

