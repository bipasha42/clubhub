package com.example.Clubhub3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        // Original User
        UserDetails userNibir = User.builder()
                .username("nibir")
                .password(passwordEncoder.encode("lmao"))
                .roles("USER", "ADMIN", "UNIADMIN")
                .build();

        // New User 1
        UserDetails userRifat = User.builder()
                .username("rifat")
                .password(passwordEncoder.encode("dsi"))
                .roles("USER") // Assigning a basic USER role
                .build();

        // New User 2
        UserDetails userBup = User.builder()
                .username("bup")
                .password(passwordEncoder.encode("dsi"))
                .roles("USER") // Assigning a basic USER role
                .build();

        // The InMemoryUserDetailsManager can accept multiple users
        return new InMemoryUserDetailsManager(userNibir, userRifat, userBup);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/clubs", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for testing

        return http.build();
    }
}