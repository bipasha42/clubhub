package com.example.clubhub.controller;

import com.example.clubhub.model.Club;
import com.example.clubhub.repository.ClubRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class ClubPageController {

    private final ClubRepository clubRepository;

    public ClubPageController(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    // Show all clubs, with optional search by name
    @GetMapping("/clubs")
    public String listClubs(@RequestParam(required = false) String name, Model model) {
        List<Club> clubs;
        if (name != null && !name.isEmpty()) {
            clubs = clubRepository.findByNameContainingIgnoreCase(name);
        } else {
            clubs = clubRepository.findAll();
        }
        model.addAttribute("clubs", clubs);
        return "club-list";
    }

    // Show info for a single club
    @GetMapping("/clubs/{id}")
    public String clubInfo(@PathVariable UUID id, Model model) {
        Club club = clubRepository.findById(id).orElse(null);
        model.addAttribute("club", club);
        // You can add members and posts here later!
        return "club-info";
    }
}