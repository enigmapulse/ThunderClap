package com.enigmapulse.thunderclap.services;

import java.util.Optional;

import com.enigmapulse.thunderclap.models.AppUser;
import com.enigmapulse.thunderclap.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // This method is called by Spring Security during authentication.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        AppUser appUser = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Create a Spring Security User object using the username, encoded password, and granted authorities.
        // Here we assign a default "USER" role. Though not needed in our case this is how it is usually implemented so who cares
        // In future we may assign someone with "ADMIN" role with more access of course
        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
