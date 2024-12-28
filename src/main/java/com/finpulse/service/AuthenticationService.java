package com.finpulse.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.finpulse.model.User;
import com.finpulse.model.UserRole;
import com.finpulse.repository.UserRepository;

import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(String username, String email, String password) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(Set.of(UserRole.USER));

        userRepository.save(newUser);
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // This will return the username
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(getLoggedInUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updateUser(User currentUser) {
        userRepository.save(currentUser);
    }

    public void deleteUser(User currentUser) {
        userRepository.delete(currentUser);
    }

    public boolean isOnboardingCompleted(User user) {
        return user.isOnboardingCompleted();
    }

    public boolean userExists(String usernameFieldValue) {
        return userRepository.existsByUsername(usernameFieldValue);
    }
}