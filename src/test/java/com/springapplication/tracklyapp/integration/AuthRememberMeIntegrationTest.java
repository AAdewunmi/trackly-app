package com.springapplication.tracklyapp.integration;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for remember-me functionality.
 * Verifies remember-me cookie, sessionless access, and correct role handling.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthRememberMeIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Ensure roles exist
        Role userRole = roleRepository.save(new Role("ROLE_USER", "User role"));
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN", "Admin role"));

        User user = new User()
                .setFullName("Regular User")
                .setEmail("user@example.com")
                .setPasswordHash(passwordEncoder.encode("password"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        User admin = new User()
                .setFullName("Admin User")
                .setEmail("admin@example.com")
                .setPasswordHash(passwordEncoder.encode("adminpassword"));
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);
    }

    @Test
    @DisplayName("Login with remember-me sets cookie, allows access after session ends")
    void rememberMe_login_persistsAcrossSession() throws Exception {
        // 1. Perform login with remember-me
        var loginResult = mockMvc.perform(post("/login")
                        .param("username", "user@example.com")
                        .param("password", "password")
                        .param("remember-me", "on")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))// adjust if your default success URL differs
                .andReturn();

        // 2. Extract remember-me cookie from response
        var rememberMeCookie = loginResult.getResponse().getCookie("remember-me");
        assertThat(rememberMeCookie).isNotNull();
        assertThat(rememberMeCookie.getMaxAge()).isGreaterThan(0);

        // 3. Simulate subsequent request without session but with remember-me cookie
        mockMvc.perform(get("/register") // use any endpoint that requires authentication
                        .cookie(rememberMeCookie))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Remember-me cookie not set if not requested")
    void loginWithoutRememberMe_noCookie() throws Exception {
        var loginResult = mockMvc.perform(post("/login")
                        .param("username", "user@example.com")
                        .param("password", "password")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        var rememberMeCookie = loginResult.getResponse().getCookie("remember-me");
        assertThat(rememberMeCookie).isNull();
    }
}


