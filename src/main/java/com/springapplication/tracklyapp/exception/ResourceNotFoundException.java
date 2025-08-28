package com.springapplication.tracklyapp.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource (e.g. user, job, application)
 * is not found in the database.
 *
 * <p>
 * This exception should be thrown from service or repository layers
 * to indicate a 404 Not Found scenario in REST APIs.
 * </p>
 *
 * <pre>
 * Example:
 *   throw new ResourceNotFoundException("User", "id", userId);
 * </pre>
 *
 * @author YourName
 */
public class ResourceNotFoundException extends TracklyApiException {

    /**
     * Constructs a new {@code ResourceNotFoundException} with a detailed message.
     *
     * @param resourceName the type of resource (e.g. "User", "Job")
     * @param fieldName the name of the lookup field (e.g. "id", "email")
     * @param fieldValue the value that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(HttpStatus.NOT_FOUND, String.format("%s not found with %s='%s'",
                resourceName, fieldName, fieldValue));
    }

    /**
     * Constructs a new {@code ResourceNotFoundException} with a custom message.
     *
     * @param message custom error message
     */
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

