package com.example.business_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Save or update a user (no hashing)
    public User saveOrUpdateUser(User user) {
        // Save the user without hashing the password
        return userRepository.save(user);
    }

    // Authenticate the user without BCrypt (plain text)
    public boolean authenticateUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Compare plain-text passwords
            if (password.equals(user.getPassword())) {
                return true;  // Password matches
            }
        }

        return false;  // Invalid credentials
    }

    // Get user role by username
    public String getUserRole(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();  // Unwrap the Optional<User>
            return user.getRole().getName(); // Assuming the role has a getName() method
        }

        return null;  // Return null if the user doesn't exist
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id); // Deletes a user by their ID
    }
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        if (username != null) {
            return userRepository.findByUsername(username).orElse(null);
        }

        return null;
    }
}
