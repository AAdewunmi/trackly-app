package com.springapplication.tracklyapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Global Spring Security configuration for Trackly.
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Expose {@link PasswordEncoder} bean for use in services</li>
 *     <li>Define HTTP security rules (public vs. protected endpoints)</li>
 *     <li>Configure default login and logout behavior</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    /**
     * Configure HTTP security rules.
     *
     * @param http HttpSecurity builder
     * @return security filter chain
     * @throws Exception if misconfigured
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for now (re-enable when adding forms)
                .csrf(csrf -> csrf.disable())
                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll() // Public
                        .anyRequest().authenticated() // Everything else requires login
                )
                // Form login with default login page
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                )
                // Logout support
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}

