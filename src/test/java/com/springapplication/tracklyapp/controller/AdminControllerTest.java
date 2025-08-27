package com.springapplication.tracklyapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
@Import(AdminControllerTest.TestViewResolverConfig.class)
class AdminControllerTest {

    @TestConfiguration
    static class TestViewResolverConfig {
        @Bean
        public ViewResolver viewResolver() {
            // Dummy resolver, avoids Thymeleaf rendering errors in test
            return (viewName, locale) -> (model, request, response) -> {
                response.getWriter().write("<html><body>Admin Dashboard</body></html>");
            };
        }
    }

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Authenticated admin user can access admin dashboard")
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void adminAccess_ok() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("email", "roles", "metrics"))
                .andExpect(model().attribute("email", "admin@example.com"))
                .andExpect(content().string(containsString("Admin Dashboard")));
    }

    @Test
    @DisplayName("Anonymous user is unauthorized for /admin")
    @WithAnonymousUser
    void anonymous_unauthorized() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized()); // 401 for HTTP Basic, not redirect
    }
}
