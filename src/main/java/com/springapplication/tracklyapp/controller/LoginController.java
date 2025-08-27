package com.springapplication.tracklyapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles GET requests for the login page.
 * - Serves login.html with support for authentication alerts.
 * - Supports query parameters: ?error, ?logout, ?registered, ?session
 * - Always uses email as username.
 */
@Controller
public class LoginController {

    /**
     * Displays the login page.
     *
     * @param error      true if login failed
     * @param logout     true if user logged out
     * @param registered true if user just registered
     * @param session    true if session expired/timed out
     * @param model      the model to inject alert flags/messages
     * @return the login page view
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            @RequestParam(value = "session", required = false) String session,
            Model model) {

        // Fixed: Check for parameter presence, not just null/empty
        if (error != null) {
            model.addAttribute("error", "Invalid email or password.");
        }
        if (logout != null) {
            model.addAttribute("logout", "You have been logged out.");
        }
        if (registered != null) {
            model.addAttribute("registered", "Account created! Please log in.");
        }
        if (session != null) {
            model.addAttribute("session", "Your session expired. Please log in again.");
        }

        model.addAttribute("usernameLabel", "Email address");
        return "login";

    }

}

