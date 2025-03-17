package com.enigmapulse.thunderclap.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.enigmapulse.thunderclap.models.AppUser;
import com.enigmapulse.thunderclap.repo.UserRepository;
import com.enigmapulse.thunderclap.services.UserListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserListService userListService;

    // this is the endpoint which returns the current logged-in username
    @GetMapping("/username")
    public String getUsername(Principal principal) {
        return principal != null ? principal.getName() : "Anonymous";
    }

    @GetMapping("/search")
    // If the user wants to search for say, Alice, the partial name would be something like
    // 'Alic' and we would return that.
    public List<String> getUsersForSearch(@RequestParam String query) {
        List<AppUser> userList = userListService.getUsers(query);
        if (!userList.isEmpty()) {
            return userList.stream()
                    .map(AppUser::getUsername)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }
}
