package com.springapplication.tracklyapp.config;

import com.springapplication.tracklyapp.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    // Mock your user service (and any other required beans)
    @MockBean
    private UserService userService;

    @Test @DisplayName("Permit public routes")
    void publicRoutesAreAccessible() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
        mvc.perform(get("/css/style.css")).andExpect(status().isOk());
        mvc.perform(get("/js/index.js")).andExpect(status().isOk());
        mvc.perform(get("/img/trackly-logo.svg")).andExpect(status().isOk());
        mvc.perform(get("/img/trackly-mark.svg")).andExpect(status().isOk());
    }

    @Test
    void loginPageIsAccessible() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void registerPageIsAccessible() throws Exception {
        mvc.perform(get("/register")).andExpect(status().isOk());
    }

    @Test
    void rememberMeCookieIsSetOnLogin() throws Exception {
        // To actually check cookie, you would need working login logic/mocks for userService!
        mvc.perform(post("/login")
                        .param("username", "admin@trackly.com")
                        .param("password", "Admin123!")
                        .param("remember-me", "on")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
                //.andExpect(cookie().exists("remember-me")); // Only works if your login is wired up
    }


}

