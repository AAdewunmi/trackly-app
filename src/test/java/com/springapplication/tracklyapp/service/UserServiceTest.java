package com.springapplication.tracklyapp.service;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userService = new UserService(userRepository, roleRepository, new BCryptPasswordEncoder());
    }

    @Test
    void testRegisterNewUser_shouldSaveUserWithHashedPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("plainpassword");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role("ROLE_USER", "default")));
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.registerNewUser(user);

        assertThat(saved.getPasswordHash()).isNotEqualTo("plainpassword");
        assertThat(saved.getRoles()).hasSize(1);
        verify(userRepository).save(saved);
    }

    @Test
    void testRegisterNewUser_shouldThrowIfEmailExists() {
        User user = new User();
        user.setEmail("duplicate@example.com");

        when(userRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerNewUser(user));
    }
}
