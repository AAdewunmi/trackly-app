package com.springapplication.tracklyapp.controller;

import com.springapplication.tracklyapp.dto.AdminMetrics;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling admin dashboard requests.
 */
@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        // Example: in real use, fetch these from your services
        AdminMetrics metrics = new AdminMetrics(
                100, // totalUsers
                7,   // deltaUsers
                3,   // activeSessions
                0.5, // errorRate (%)
                2    // queueSize
        );

        model.addAttribute("metrics", metrics);
        model.addAttribute("email", "admin@example.com");
        model.addAttribute("roles", java.util.List.of("ADMIN"));

        return "admin"; // Thymeleaf template
    }
}
