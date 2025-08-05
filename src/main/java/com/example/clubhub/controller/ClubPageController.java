package com.example.clubhub.controller;

import com.example.clubhub.model.Club;
import com.example.clubhub.model.User;
import com.example.clubhub.model.Post;
import com.example.clubhub.repository.ClubRepository;
import com.example.clubhub.repository.UserRepository;
import com.example.clubhub.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class ClubPageController {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ClubPageController(ClubRepository clubRepository, UserRepository userRepository, PostRepository postRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

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

    @GetMapping("/clubs/{id}")
    public String clubInfo(@PathVariable UUID id, Model model) {
        Club club = clubRepository.findById(id).orElse(null);
        List<User> members = userRepository.findByClubId(id);
        List<Post> posts = postRepository.findByClubId(id);
        model.addAttribute("club", club);
        model.addAttribute("members", members);
        model.addAttribute("posts", posts);
        return "club-info";
    }
}