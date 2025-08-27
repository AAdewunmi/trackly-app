package com.springapplication.tracklyapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MVC slice test for {@link LogoutController}.
 */
@WebMvcTest(controllers = LogoutController.class)
@AutoConfigureMockMvc(addFilters = false) // keep Spring Security filters out of the way for this view test
class LogoutControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("GET /logout-success renders logout view with CTA to /login")
    void logoutSuccess_RendersPage() throws Exception {
        mvc.perform(get("/logout-success"))
                .andExpect(status().isOk())
                .andExpect(view().name("logout"))
                .andExpect(content().string(containsString("You’re signed out")))
                .andExpect(content().string(containsString("Back to login")))
                .andExpect(content().string(containsString("/login")));
    }
}

