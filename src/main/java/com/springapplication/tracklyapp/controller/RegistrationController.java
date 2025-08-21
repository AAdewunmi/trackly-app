package com.springapplication.tracklyapp.controller;

import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling registration endpoints.
 */
@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to register a new user.
     * @param user user payload
     * @return created user
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User registered = userService.registerNewUser(user);
        return ResponseEntity.ok(registered);
    }
}

