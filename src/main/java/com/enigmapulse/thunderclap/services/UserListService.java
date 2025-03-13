package com.enigmapulse.thunderclap.services;

import com.enigmapulse.thunderclap.models.AppUser;
import com.enigmapulse.thunderclap.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserListService {

    @Autowired
    private UserRepository userRepository;

    // Return a username that contains 'partialName' when searching in the search bar of private chat
    public List<AppUser> getUsers(String partialName) {
        return userRepository.findByUsernameContaining(partialName);
    }
}
