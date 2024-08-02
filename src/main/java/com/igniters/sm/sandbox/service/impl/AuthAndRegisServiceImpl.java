package com.igniters.sm.sandbox.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.igniters.sm.sandbox.dao.RoleRepository;
import com.igniters.sm.sandbox.dao.UserRepository;
import com.igniters.sm.sandbox.model.Role;
import com.igniters.sm.sandbox.model.User;
import com.igniters.sm.sandbox.service.AuthAndRegisService;

@Service

public class AuthAndRegisServiceImpl implements AuthAndRegisService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {

            System.out.println("User already exists");
            return false;
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            // Save the new user with role
            List<Role> roles = user.getRoles();
            if (roles != null && !roles.isEmpty()) {
                for (Role role : roles) {
                    roleRepository.save(role);
                }

            }
            return userRepository.save(user) != null;
        } catch (Exception e) {

            System.err.println("Error occurred while saving user: " + e.getMessage());
            return false;
        }

    }
}