package com.enigmapulse.thunderclap.configurations;

import com.enigmapulse.thunderclap.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //we have made a custom service where apart from what the JPA repository offers
    //we have defined our custom function for our database loadUserByUsername
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access to these endpoints (including static pages and registration)
                        .requestMatchers("/", "/username", "/login", "/welcome.html","/welcome", "/registration.html", "/register", "/h2-console/**", "/main.js", "/sockjs/**", "/ws/**").permitAll()
                        .anyRequest().authenticated()
                )
                // we are using spring's default login page instead of making our own
                .formLogin(Customizer.withDefaults())
                // allow anyone to logout
                .logout(LogoutConfigurer::permitAll)
                //A cross-site request forgery (CSRF) attack is a malicious exploit
                // that tricks a user into performing unwanted actions on a website.
                // we disable it through below method
                .csrf(AbstractHttpConfigurer::disable)
                // Allow H2 Console to be loaded in a frame (no idea wth this is)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }

    // we are storing passwords after encrypting in our database for obvious security reasons
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
