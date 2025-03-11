package com.enigmapulse.thunderclap.controllers;

import com.enigmapulse.thunderclap.models.AppUser;
import com.enigmapulse.thunderclap.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    // we use repository to save data into our database
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Provided via Security Config login


    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            // currently it's not showing any error just redirects to this page
            // we will look into it later
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
