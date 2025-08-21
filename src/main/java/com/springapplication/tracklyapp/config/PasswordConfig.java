package com.springapplication.tracklyapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security-related bean configuration.
 * <p>
 * Defines a single {@link PasswordEncoder} bean used by the registration flow
 * and authentication.
 */
@Configuration
public class PasswordConfig {

    /**
     * BCrypt with a reasonable default strength (10).
     * Increase cost if needed depending on performance envelope.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

