package com.springapplication.tracklyapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Authentication endpoints for login and registration pages.
 * <p>
 * These handlers return the view names that Thymeleaf resolves to templates:
 * <ul>
 *   <li>/login     -> templates/login.html</li>
 *   <li>/register  -> templates/register.html</li>
 * </ul>
 */
@Controller
public class AuthController {

    /**
     * Render the login page.
     * @return view name "login" (templates/login.html)
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Render the registration page.
     * @return view name "register" (templates/register.html)
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }
}

