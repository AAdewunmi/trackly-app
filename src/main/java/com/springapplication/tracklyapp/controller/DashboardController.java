package com.springapplication.tracklyapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    @ResponseBody // For testing, just return plain text
    public String dashboard() {
        return "Dashboard!";
    }

    @GetMapping("/jobs")
    @ResponseBody
    public String jobs() {
        return "Jobs!";
    }
}

