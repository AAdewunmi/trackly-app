// File: src/test/java/com/springapplication/tracklyapp/integration/AuthLogoutIntegrationTest.java

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthLogoutIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Create test roles
        Role userRole = roleRepository.save(new Role("ROLE_USER", "User role"));
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN", "Admin role"));

        // Create test users
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
    @DisplayName("ROLE_USER logout flow")
    class UserLogoutFlow {
        @Test
        @DisplayName("User can log in, log out, and is redirected. After logout, protected URL redirects to /login")
        void userLogoutFlow() throws Exception {
            // 1. Login as user and save session
            var loginResult = mockMvc.perform(post("/login")
                            .param("username", "user@example.com")
                            .param("password", "password")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/dashboard"))
                    .andReturn();

            MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
            assertThat(session).isNotNull();
            // 2. Logout with session
            var logoutResult = mockMvc.perform(post("/logout")
                            .session(session) // <<----- Just pass session object!
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?logout"))
                    .andReturn();

            // 3. After logout, session is invalidated
            assertThat(logoutResult.getRequest().getSession(false)).isNull();

            // 4. Accessing a protected resource redirects to /login
            mockMvc.perform(get("/dashboard"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }

    @Nested
    @DisplayName("ROLE_ADMIN logout flow")
    class AdminLogoutFlow {
        @Test
        @DisplayName("Admin can log in, log out, and is redirected. After logout, protected URL redirects to /login")
        void adminLogoutFlow() throws Exception {
            // 1. Login as admin and save session
            var loginResult = mockMvc.perform(post("/login")
                            .param("username", "admin@example.com")
                            .param("password", "adminpassword")
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/dashboard"))
                    .andReturn();

            MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
            assertThat(session).isNotNull();

            // 2. Logout with session
            var logoutResult = mockMvc.perform(post("/logout")
                            .session(session) // <<----- Just pass session object!
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login?logout"))
                    .andReturn();

            // 3. After logout, session is invalidated
            assertThat(logoutResult.getRequest().getSession(false)).isNull();

            // 4. Accessing a protected resource redirects to /login
            mockMvc.perform(get("/admin/dashboard"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrlPattern("**/login"));
        }
    }
}
