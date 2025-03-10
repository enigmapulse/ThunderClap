package com.enigmapulse.thunderclap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // Our custom service

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access to these endpoints (including static pages and registration)
                        .requestMatchers("/", "/login", "/welcome.html", "/registration.html", "/register", "/h2-console/**", "/main.js", "/sockjs/**", "/ws/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Remove custom loginPage so Spring Security uses its default login page
                .httpBasic(Customizer.withDefaults())
                .logout(logout -> logout.permitAll())
                .csrf(AbstractHttpConfigurer::disable);
                // Allow H2 Console to be loaded in a frame\
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
