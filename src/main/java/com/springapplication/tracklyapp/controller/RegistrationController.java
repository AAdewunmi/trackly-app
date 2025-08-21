package com.springapplication.tracklyapp.controller;

import com.springapplication.tracklyapp.service.UserService;
import com.springapplication.tracklyapp.dto.RegistrationForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * MVC controller for user registration using a Thymeleaf form.
 */
@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) return "register";
        try {
            userService.register(form.getFullName(), form.getEmail(), form.getPassword());
            return "redirect:/login?registered";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "register";
        }
    }
}
