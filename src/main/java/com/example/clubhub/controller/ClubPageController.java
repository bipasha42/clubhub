package com.example.clubhub.controller;

import com.example.clubhub.model.Club;
import com.example.clubhub.model.User;
import com.example.clubhub.model.Post;
import com.example.clubhub.model.University;
import com.example.clubhub.repository.ClubRepository;
import com.example.clubhub.repository.UserRepository;
import com.example.clubhub.repository.PostRepository;
import com.example.clubhub.repository.UniversityRepository;
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
    private final UniversityRepository universityRepository;

    public ClubPageController(
            ClubRepository clubRepository,
            UserRepository userRepository,
            PostRepository postRepository,
            UniversityRepository universityRepository
    ) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.universityRepository = universityRepository;
    }

    @GetMapping("/clubs")
    public String listClubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) UUID universityId,
            @RequestParam(required = false) Boolean isApproved,
            Model model) {

        List<Club> clubs = clubRepository.findAll();
        if (name != null && !name.isEmpty()) {
            clubs = clubs.stream()
                    .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }
        if (category != null && !category.isEmpty()) {
            clubs = clubs.stream()
                    .filter(c -> c.getCategory() != null && c.getCategory().equalsIgnoreCase(category))
                    .toList();
        }
        if (universityId != null) {
            clubs = clubs.stream()
                    .filter(c -> c.getUniversity() != null && universityId.equals(c.getUniversity().getUniversityId()))
                    .toList();
        }
        if (isApproved != null) {
            clubs = clubs.stream()
                    .filter(c -> c.getIsApproved() != null && c.getIsApproved().equals(isApproved))
                    .toList();
        }

        List<University> universities = universityRepository.findAll();
        model.addAttribute("clubs", clubs);
        model.addAttribute("universities", universities);
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