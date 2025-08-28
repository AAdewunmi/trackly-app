package com.springapplication.tracklyapp.controller;

import com.springapplication.tracklyapp.exception.ResourceNotFoundException;
import com.springapplication.tracklyapp.exception.TracklyApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestApiExceptionController {

    @GetMapping("/not-found")
    public void throwNotFound() {
        throw new ResourceNotFoundException("Resource not found"); // <-- Update to expected message
    }

    @GetMapping("/bad-request")
    public void throwBadRequest() {
        throw new TracklyApiException(HttpStatus.BAD_REQUEST, "Invalid payload"); // <-- Update to expected message
    }
}
