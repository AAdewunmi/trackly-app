package com.springapplication.tracklyapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for {@link SecurityConfig}.
 */
@WebMvcTest
@Import({SecurityConfig.class, PasswordConfig.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoder_shouldEncodeAndMatch() {
        String raw = "password123";
        String hash = passwordEncoder.encode(raw);

        assertThat(passwordEncoder.matches(raw, hash)).isTrue();
    }

    @Test
    void unauthenticated_shouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection()); // redirect to login
    }

    @Test
    void loginAndRegisterPages_shouldBePublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }
}

