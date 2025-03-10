package com.enigmapulse.thunderclap;

import java.util.Collections;
import java.util.Optional;
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
        // Here we assign a default "USER" role. You can customize this as needed.
        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
