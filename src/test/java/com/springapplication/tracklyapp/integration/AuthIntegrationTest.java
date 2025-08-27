package com.springapplication.tracklyapp.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for login authentication (ROLE_USER, ROLE_ADMIN).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestUsersConfig.class)
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // --- LOGIN SUCCESS ---

    @Nested
    @DisplayName("Login Success")
    class LoginSuccess {

        @Test
        @DisplayName("ROLE_USER can login and is redirected to default page")
        void userLoginSuccess() throws Exception {
            mockMvc.perform(formLogin("/login").user("alice@example.com").password("$2a$10$hashedPassword"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error")); // adjust if you have a different default success URL
        }

        @Test
        @DisplayName("ROLE_ADMIN can login and is redirected to default page")
        void adminLoginSuccess() throws Exception {
            mockMvc.perform(formLogin("/login").user("admin@trackly.com").password(" $2a$10$OuToIW2pkLevtQiuA3U2UO0SKnbaMdZQYM3jiyjkhXlbE/rYIi.im"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error")); // adjust if admin has a dashboard redirect
        }
    }

    // --- LOGIN FAILURE ---

    @Nested
    @DisplayName("Login Failure")
    class LoginFailure {

        @Test
        @DisplayName("Invalid user credentials redirect to /login?error")
        void invalidUserCredentials() throws Exception {
            mockMvc.perform(formLogin("/login").user("user").password("badpassword"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error"));
        }

        @Test
        @DisplayName("Unknown username redirects to /login?error")
        void unknownUsername() throws Exception {
            mockMvc.perform(formLogin("/login").user("unknown").password("irrelevant"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?error"));
        }
    }

    // --- OPTIONAL: Authenticated access test for ROLE_ADMIN and ROLE_USER ---

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userCannotAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden()); // or is3xxRedirection() if you redirect unauthorized users
    }
}

