package com.springapplication.tracklyapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

/**
 * Controller for admin-only dashboard access.
 * Serves a placeholder page displaying the logged-in user's email and roles.
 *
 * Accessible only to users with ROLE_ADMIN.
 */
@Controller
public class AdminController {

    /**
     * Serves the /admin dashboard page.
     *
     * @param auth  the Spring Security authentication object containing user details
     * @param model the view model
     * @return the admin.html template
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminDashboard(Authentication auth, Model model) {
        model.addAttribute("email", auth.getName()); // username/email
        model.addAttribute("roles", auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return "admin";
    }
}

