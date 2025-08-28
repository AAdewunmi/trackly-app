package com.springapplication.tracklyapp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link TracklyApiException}.
 */
class TracklyApiExceptionTest {

    @Test
    @DisplayName("Stores status and message")
    void storesStatusAndMessage() {
        TracklyApiException ex = new TracklyApiException(HttpStatus.BAD_REQUEST, "Invalid input");
        assertThat(ex.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ex.getMessage()).isEqualTo("Invalid input");
        assertThat(ex.getCause()).isNull();
    }

    @Test
    @DisplayName("Supports cause")
    void supportsCause() {
        RuntimeException cause = new RuntimeException("root");
        TracklyApiException ex = new TracklyApiException(HttpStatus.CONFLICT, "Conflict", cause);
        assertThat(ex.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(ex.getMessage()).isEqualTo("Conflict");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    @DisplayName("Null arguments are rejected")
    void nullArgumentsRejected() {
        assertThrows(NullPointerException.class, () -> new TracklyApiException(null, "msg"));
        assertThrows(NullPointerException.class, () -> new TracklyApiException(HttpStatus.I_AM_A_TEAPOT, null));
        assertThrows(NullPointerException.class, () -> new TracklyApiException(null, "msg", new RuntimeException()));
        assertThrows(NullPointerException.class, () -> new TracklyApiException(HttpStatus.NOT_FOUND, null, new RuntimeException()));
    }
}

