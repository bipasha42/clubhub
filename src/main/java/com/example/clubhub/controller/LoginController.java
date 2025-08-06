package com.example.clubhub.controller;

import com.example.clubhub.model.User;
import com.example.clubhub.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession; // Use javax.servlet.http.HttpSession if you are on older Spring Boot

@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String passwordHash, HttpSession session) {
        User user = loginRepository.findByEmailAndPasswordHash(email, passwordHash);
        if (user != null) {
            session.setAttribute("currentUser", user); // Store user in session
            if (user.isAdmin()) {
                return "redirect:/crud";
            } else {
                return "redirect:/student_dashboard";
            }
        }
        return "redirect:/login?error";
    }
}