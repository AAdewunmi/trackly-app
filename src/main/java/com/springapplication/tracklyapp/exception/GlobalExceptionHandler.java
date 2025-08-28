package com.springapplication.tracklyapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Translates exceptions into standardized API JSON error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TracklyApiException.class)
    public ResponseEntity<Map<String, Object>> handleTracklyApiException(
            TracklyApiException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = ex.getStatus();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("path", (request != null && StringUtils.hasText(request.getRequestURI()))
                ? request.getRequestURI() : "");

        return ResponseEntity.status(status).body(body);
    }

    /**
     * Optionally: catch-all to avoid HTML error pages leaking to API clients.
     * You can remove this if you already have one.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherExceptions(
            Exception ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("path", (request != null && request.getRequestURI() != null) ? request.getRequestURI() : "");
        return ResponseEntity.status(status).body(body);
    }
}

