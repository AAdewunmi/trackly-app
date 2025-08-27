package com.springapplication.tracklyapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves a friendly logout landing page.
 * Map your Spring Security logout success to this endpoint (e.g. "/logout-success").
 */
@Controller
public class LogoutController {

    /**
     * GET /logout-success -> renders logout.html with optional message.
     * @param model view model
     * @return "logout" Thymeleaf view
     */
    @GetMapping("/logout-success")
    public String logoutSuccess(Model model) {
        model.addAttribute("message", "You have been signed out.");
        return "logout";
    }
}

