package com.springapplication.tracklyapp.controller;

import com.springapplication.tracklyapp.dto.RegistrationForm;
import com.springapplication.tracklyapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    private RegistrationController registrationController;
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        registrationController = new RegistrationController(userService);
    }

    @Test
    void registerUser_SuccessfulRegistration_RedirectToLogin() {
        RegistrationForm form = new RegistrationForm();
        form.setFullName("John Doe");
        form.setEmail("john.doe@example.com");
        form.setPassword("Password1");

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);

        when(result.hasErrors()).thenReturn(false);

        String view = registrationController.registerUser(form, result, model);

        assertEquals("redirect:/login?registered", view);
        verify(userService, times(1)).register(form.getFullName(), form.getEmail(), form.getPassword());
        verifyNoInteractions(model);
    }

    @Test
    void registerUser_FormValidationErrors_ReturnRegisterView() {
        RegistrationForm form = new RegistrationForm();
        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);

        when(result.hasErrors()).thenReturn(true);

        String view = registrationController.registerUser(form, result, model);

        assertEquals("register", view);
        verifyNoInteractions(userService);
        verifyNoInteractions(model);
    }

    @Test
    void registerUser_ExceptionThrown_ReturnRegisterViewWithErrorMessage() {
        RegistrationForm form = new RegistrationForm();
        form.setFullName("John Doe");
        form.setEmail("invalid-email");
        form.setPassword("Password1");

        BindingResult result = mock(BindingResult.class);
        Model model = mock(Model.class);

        when(result.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("Email already exists"))
                .when(userService)
                .register(form.getFullName(), form.getEmail(), form.getPassword());

        String view = registrationController.registerUser(form, result, model);

        assertEquals("register", view);
        verify(model, times(1)).addAttribute("errorMessage", "Email already exists");
    }
}