package com.springapplication.tracklyapp.controller;

import com.springapplication.tracklyapp.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /login returns login page")
    void loginPage_Default() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error", "logout", "registered", "session"))
                .andExpect(model().attribute("usernameLabel", "Email address"));
    }

    @Test
    @DisplayName("GET /login?error returns login page with error")
    void loginPage_Error() throws Exception {
        mockMvc.perform(get("/login").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Invalid email or password."))
                .andExpect(model().attribute("usernameLabel", "Email address"));
    }

    @Test
    @DisplayName("GET /login?logout returns login page with logout message")
    void loginPage_Logout() throws Exception {
        mockMvc.perform(get("/login").param("logout", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("logout"))
                .andExpect(model().attribute("logout", "You have been logged out."))
                .andExpect(model().attribute("usernameLabel", "Email address"));
    }

    @Test
    @DisplayName("GET /login?registered returns login page with registered message")
    void loginPage_Registered() throws Exception {
        mockMvc.perform(get("/login").param("registered", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registered"))
                .andExpect(model().attribute("registered", "Account created! Please log in."))
                .andExpect(model().attribute("usernameLabel", "Email address"));
    }

    @Test
    @DisplayName("GET /login?session returns login page with session expired message")
    void loginPage_SessionExpired() throws Exception {
        mockMvc.perform(get("/login").param("session", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("session"))
                .andExpect(model().attribute("session", "Your session expired. Please log in again."))
                .andExpect(model().attribute("usernameLabel", "Email address"));
    }
}