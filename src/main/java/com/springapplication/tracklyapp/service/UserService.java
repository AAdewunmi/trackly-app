package com.springapplication.tracklyapp.service;

import com.springapplication.tracklyapp.model.Role;
import com.springapplication.tracklyapp.model.User;
import com.springapplication.tracklyapp.repository.RoleRepository;
import com.springapplication.tracklyapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, RoleRepository roles, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String fullName, String email, String rawPassword) {
        String normEmail = email.trim().toLowerCase();
        if (users.existsByEmail(normEmail)) {
            throw new IllegalArgumentException("Email already in use.");
        }
        if (rawPassword == null || rawPassword.length() < 8 || !rawPassword.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("Password must be at least 8 chars and include a letter and a number.");
        }

        String hash = passwordEncoder.encode(rawPassword);
        Role roleUser = roles.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(normEmail);
        user.setPasswordHash(hash);
        user.getRoles().add(roleUser);
        return users.save(user);
    }
}