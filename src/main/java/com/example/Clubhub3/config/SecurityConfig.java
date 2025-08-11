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
        // Original User - University Admin
        UserDetails userNibir = User.builder()
                .username("nibir@example.com") // Changed to match the email in database
                .password(passwordEncoder.encode("lmao"))
                .roles("USER", "ADMIN", "UNIADMIN")
                .build();

        // Student User 1
        UserDetails userRifat = User.builder()
                .username("rifat@example.com") // Changed to match the email in database
                .password(passwordEncoder.encode("dsi"))
                .roles("USER")
                .build();

        // Student User 2
        UserDetails userBup = User.builder()
                .username("bup@example.com") // Changed to match the email in database
                .password(passwordEncoder.encode("dsi"))
                .roles("USER")
                .build();

        // The InMemoryUserDetailsManager can accept multiple users
        return new InMemoryUserDetailsManager(userNibir, userRifat, userBup);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/clubs/explore").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/clubs", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.setStatus(401);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\": \"Please login to continue\"}");
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                );

        return http.build();
    }
}