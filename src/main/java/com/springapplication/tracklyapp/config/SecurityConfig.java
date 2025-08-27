package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures application security settings: authentication, authorisation, login/logout, session, CSRF.
 *
 * - Public: /login, /register, /css/**, /js/**, /img/**
 * - Authenticated: all other endpoints
 * - Form login: custom login.html, with redirect on success/failure
 */
@Configuration
public class SecurityConfig {

    /**
     * Password encoder using BCrypt (strength 10).
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Custom user details service for authentication.
     */
    @Bean
    public UserDetailsService userDetailsService(CustomUserDetailsService delegate) {
        return delegate;
    }

    /**
     * DAO authentication provider setup.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    /**
     * Configures the main security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomUserDetailsService uds) throws Exception {
        http
                // CSRF enabled by default (can customize)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")) // Example: permit H2 console in dev
                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                // Form login config
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true) // Where to go after login (change as needed)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                // Logout config
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )
                // Remember-me config (optional)
                .rememberMe(rm -> rm
                        .userDetailsService(uds)
                        .key("SuperSecretTracklyRememberMeKey")   // Use env var in prod!
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)   // 7 days
                )
                // Session management (optional hardening)
                .sessionManagement(session -> session
                        .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
                        .maximumSessions(3)
                        .maxSessionsPreventsLogin(false)
                );
        return http.build();
    }
}

