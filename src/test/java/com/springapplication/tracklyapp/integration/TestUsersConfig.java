package com.springapplication.tracklyapp.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class TestUsersConfig {
    @Bean
    public UserDetailsService testUserDetailsService() {
        var user  = User.withUsername("user").password("{noop}password").roles("USER").build();
        var admin = User.withUsername("admin").password("{noop}adminpassword").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}

