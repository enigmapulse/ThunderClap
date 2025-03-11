package com.enigmapulse.thunderclap.controllers;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // this is the endpoint which returns the current logged-in username
    @GetMapping("/username")
    public String getUsername(Principal principal) {
        return principal != null ? principal.getName() : "Anonymous";
    }
}
