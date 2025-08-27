package com.springapplication.tracklyapp.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies public and protected route access per security config.
 */
@WebMvcTest
@Import(SecurityConfig.class)
class SecurityConfigTest {
    @Autowired MockMvc mvc;

    @Test @DisplayName("Permit public routes")
    void publicRoutesAreAccessible() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
        mvc.perform(get("/register")).andExpect(status().isOk());
        mvc.perform(get("/css/main.css")).andExpect(status().isOk());
        mvc.perform(get("/js/app.js")).andExpect(status().isOk());
        mvc.perform(get("/img/logo.png")).andExpect(status().isOk());
    }

    @Test @DisplayName("Protect other routes")
    void protectedRoutesRequireAuth() throws Exception {
        mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/admin")).andExpect(status().is3xxRedirection());
    }

    @Test
    void loginPageIsAccessible() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void protectedPageRequiresLogin() throws Exception {
        mvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection());
    }

    @Test
    void rememberMeCookieIsSetOnLogin() throws Exception {
        mvc.perform(post("/login")
                        .param("username", "admin@trackly.com")
                        .param("password", "Admin123!")
                        .param("remember-me", "on"))
                .andExpect(cookie().exists("remember-me"));
    }

}

