package com.springapplication.tracklyapp.integration;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthProtectedRouteIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

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

    @Nested
    @DisplayName("Protected route /dashboard access")
    class DashboardRouteAccess {

        @Test
        @DisplayName("Unauthenticated user is redirected to /login")
        void unauthenticated_redirectedToLogin() throws Exception {
            mockMvc.perform(get("/dashboard"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }

        @Test
        @DisplayName("Authenticated ROLE_USER gets 200 OK")
        void authenticatedUser_accessOk() throws Exception {
            var loginResult = mockMvc.perform(post("/login")
                            .param("username", "user@example.com")
                            .param("password", "password")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

            MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
            assertThat(session).isNotNull();

            mockMvc.perform(get("/dashboard").session(session))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Authenticated ROLE_ADMIN gets 200 OK")
        void authenticatedAdmin_accessOk() throws Exception {
            var loginResult = mockMvc.perform(post("/login")
                            .param("username", "admin@example.com")
                            .param("password", "adminpassword")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

            MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
            assertThat(session).isNotNull();

            mockMvc.perform(get("/dashboard").session(session))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Protected route /jobs access")
    class JobsRouteAccess {

        @Test
        @DisplayName("Unauthenticated user is redirected to /login")
        void unauthenticated_redirectedToLogin() throws Exception {
            mockMvc.perform(get("/jobs"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }

        @Test
        @DisplayName("Authenticated ROLE_USER gets 200 OK")
        void authenticatedUser_accessOk() throws Exception {
            var loginResult = mockMvc.perform(post("/login")
                            .param("username", "user@example.com")
                            .param("password", "password")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

            MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
            assertThat(session).isNotNull();

            mockMvc.perform(get("/jobs").session(session))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Authenticated ROLE_ADMIN gets 200 OK")
        void authenticatedAdmin_accessOk() throws Exception {
            var loginResult = mockMvc.perform(post("/login")
                            .param("username", "admin@example.com")
                            .param("password", "adminpassword")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();

            MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
            assertThat(session).isNotNull();

            mockMvc.perform(get("/jobs").session(session))
                    .andExpect(status().isOk());
        }
    }
}

