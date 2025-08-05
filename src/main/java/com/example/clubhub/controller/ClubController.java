package com.example.clubhub.controller;

import com.example.clubhub.model.Club;
import com.example.clubhub.model.User;
import com.example.clubhub.model.Post;
import com.example.clubhub.repository.ClubRepository;
import com.example.clubhub.repository.UserRepository;
import com.example.clubhub.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public ClubController(ClubRepository clubRepository, UserRepository userRepository, PostRepository postRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    @PostMapping
    public Club createClub(@RequestBody Club club) {
        return clubRepository.save(club);
    }

    @GetMapping("/{id}")
    public Club getClubById(@PathVariable UUID id) {
        return clubRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/members")
    public List<User> getClubMembers(@PathVariable UUID id) {
        return userRepository.findByClubId(id);
    }

    @GetMapping("/{id}/posts")
    public List<Post> getClubPosts(@PathVariable UUID id) {
        return postRepository.findByClubId(id);
    }

    // Advanced search/filtering endpoint
    @GetMapping("/search")
    public List<Club> searchClubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) UUID universityId,
            @RequestParam(required = false) Boolean isApproved
    ) {
        Specification<Club> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (category != null) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
        }
        if (universityId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("university").get("universityId"), universityId));
        }
        if (isApproved != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isApproved"), isApproved));
        }

        return clubRepository.findAll(spec);
    }
}