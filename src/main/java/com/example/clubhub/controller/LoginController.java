package com.example.clubhub.controller;

import com.example.clubhub.model.User;
import com.example.clubhub.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private LoginRepository loginRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String passwordHash) {
        User user = loginRepository.findByEmailAndPasswordHash(email, passwordHash);
        if (user != null) {
            if (user.isAdmin()) {
                return "redirect:/crud";
            } else {
                return "redirect:/student_dashboard";
            }
        }
        return "redirect:/login?error";
    }
}