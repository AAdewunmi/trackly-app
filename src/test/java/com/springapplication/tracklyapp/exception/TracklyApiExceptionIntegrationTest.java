package com.springapplication.tracklyapp.exception;

import com.springapplication.tracklyapp.exception.GlobalExceptionHandler;
import com.springapplication.tracklyapp.exception.TracklyApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test that verifies {@link TracklyApiException} is translated
 * into a JSON error payload with the correct HTTP status by {@link GlobalExceptionHandler}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TracklyApiExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/api/test/not-found")
        public String notFound() {
            throw new TracklyApiException(HttpStatus.NOT_FOUND, "Resource not found");
        }

        @GetMapping("/api/test/bad-request")
        public String badRequest() {
            throw new TracklyApiException(HttpStatus.BAD_REQUEST, "Invalid payload");
        }
    }

    @Test
    @WithMockUser(username = "apiuser", roles = {"USER"})
    void notFoundProducesJsonError() throws Exception {
        mockMvc.perform(get("/api/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Resource not found")))
                .andExpect(jsonPath("$.path", is("/api/test/not-found")))
                .andExpect(jsonPath("$.timestamp", not(emptyOrNullString())));
    }

    @Test
    @WithMockUser(username = "apiuser", roles = {"USER"})
    void badRequestProducesJsonError() throws Exception {
        mockMvc.perform(get("/api/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Invalid payload")))
                .andExpect(jsonPath("$.path", is("/api/test/bad-request")))
                .andExpect(jsonPath("$.timestamp", not(emptyOrNullString())));
    }
}

