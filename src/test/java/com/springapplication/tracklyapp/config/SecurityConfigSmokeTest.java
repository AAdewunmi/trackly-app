package com.springapplication.tracklyapp.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Stable, full-context smoke tests for Spring Security.
 * Loads the real application context to avoid brittle @WebMvcTest slicing issues.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigSmokeTest {

    @Autowired private ApplicationContext ctx;
    @Autowired private MockMvc mockMvc;

    @Test
    @DisplayName("Exactly one PasswordEncoder bean is present")
    void passwordEncoderBean_presentAndUnique() {
        var beans = ctx.getBeansOfType(PasswordEncoder.class);
        assertThat(beans)
                .withFailMessage("Expected exactly 1 PasswordEncoder bean, found: %s", beans.keySet())
                .hasSize(1);
    }

    @Test
    @DisplayName("GET /login and /register are publicly accessible (200 OK)")
    void publicPages_accessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @DisplayName("Unauthenticated access to protected route redirects to /login")
    void protected_redirectsToLogin() throws Exception {
        // Change "/dashboard" to any route that requires auth in your app
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}


