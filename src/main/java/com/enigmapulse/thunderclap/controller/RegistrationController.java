package com.enigmapulse.thunderclap.controller;

import com.enigmapulse.thunderclap.AppUser;
import com.enigmapulse.thunderclap.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Provided via Security Config login

    @GetMapping("/registration.html")
    public String showRegistrationForm() {
        return "registration"; // Resolves to registration.html view
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        // Check if username already exists (optional)
        if (userRepository.findByUsername(username).isPresent()) {
            // You may want to handle this case, e.g. by returning an error message.
            return "redirect:/registration.html?error=usernameTaken";
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        // Encode the password before saving.
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        // After successful registration, redirect to login.
        return "redirect:/login";
    }
}
