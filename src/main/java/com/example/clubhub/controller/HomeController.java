package com.example.clubhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/student_dashboard")
    public String studentDashboard() {
        return "student_dashboard";
    }
}