package com.springapplication.tracklyapp.exception;

import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * A custom runtime exception for Trackly's API layer.
 * <p>
 * Use this exception to signal API-specific failures while carrying an
 * {@link HttpStatus} and a human-readable message. It is typically handled by
 * a global {@code @ControllerAdvice} to produce standardized JSON error
 * responses.
 *
 * <h3>Examples</h3>
 * <pre>{@code
 * // Throwing with NOT_FOUND
 * throw new TracklyApiException(HttpStatus.NOT_FOUND, "User not found");
 *
 * // Wrapping an underlying cause (e.g. repository/service exception)
 * try {
 *     service.call();
 * } catch (Exception ex) {
 *     throw new TracklyApiException(HttpStatus.BAD_GATEWAY, "Upstream service failed", ex);
 * }
 * }</pre>
 */
public class TracklyApiException extends RuntimeException {

    private final HttpStatus status;

    /**
     * Create a new exception with an HTTP status and message.
     *
     * @param status  HTTP status to return (must not be {@code null})
     * @param message human-readable error message (must not be {@code null})
     */
    public TracklyApiException(HttpStatus status, String message) {
        super(Objects.requireNonNull(message, "message must not be null"));
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    /**
     * Create a new exception with an HTTP status, message, and underlying cause.
     *
     * @param status  HTTP status to return (must not be {@code null})
     * @param message human-readable error message (must not be {@code null})
     * @param cause   the underlying cause (may be {@code null})
     */
    public TracklyApiException(HttpStatus status, String message, Throwable cause) {
        super(Objects.requireNonNull(message, "message must not be null"), cause);
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    /**
     * The HTTP status associated with this error.
     */
    public HttpStatus getStatus() {
        return status;
    }
}

