package com.example.Clubhub3.controller;

import com.example.Clubhub3.dto.ClubShowcaseDto;
import com.example.Clubhub3.dto.PostDto;
import com.example.Clubhub3.dto.ClubMemberDto;
import com.example.Clubhub3.Service.ClubShowcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/clubs")
public class ClubShowcaseController {

    @Autowired
    private ClubShowcaseService clubShowcaseService;

    @GetMapping
    public String exploreClubs(Model model) {
        try {
            UUID currentUserId = null; // TODO: Get from authentication
            List<ClubShowcaseDto> clubs = clubShowcaseService.getAllClubsForShowcase(currentUserId);
            model.addAttribute("clubs", clubs);
            System.out.println("Found " + clubs.size() + " clubs for showcase");
        } catch (Exception e) {
            System.err.println("Error loading clubs: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading clubs: " + e.getMessage());
        }
        return "clubs/explore";
    }

    @GetMapping("/{clubId}")
    public String viewClub(@PathVariable UUID clubId, Model model) {
        try {
            System.out.println("=== DEBUG: Viewing club with ID: " + clubId + " ===");
            UUID currentUserId = null; // TODO: Get from authentication

            ClubShowcaseDto club = clubShowcaseService.getClubDetails(clubId, currentUserId);
            System.out.println("=== DEBUG: Club object: " + club + " ===");

            if (club == null) {
                System.err.println("=== DEBUG: Club is null! ===");
                model.addAttribute("errorMessage", "Club not found");
                return "clubs/explore";
            }

            List<PostDto> posts = clubShowcaseService.getClubPosts(clubId, currentUserId);
            List<ClubMemberDto> members = clubShowcaseService.getClubMembers(clubId);

            System.out.println("=== DEBUG: Posts count: " + (posts != null ? posts.size() : "null") + " ===");
            System.out.println("=== DEBUG: Members count: " + (members != null ? members.size() : "null") + " ===");

            model.addAttribute("club", club);
            model.addAttribute("posts", posts);
            model.addAttribute("members", members);

            System.out.println("Viewing club: " + club.getName() + " with " + posts.size() + " posts");
        } catch (Exception e) {
            System.err.println("=== DEBUG: Exception in viewClub: " + e.getMessage() + " ===");
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading club details: " + e.getMessage());
        }
        return "clubs/details";
    }

    @GetMapping("/posts/{postId}")
    public String viewPost(@PathVariable UUID postId, Model model) {
        try {
            UUID currentUserId = null; // TODO: Get from authentication

            PostDto post = clubShowcaseService.getPostDetails(postId, currentUserId);
            if (post == null) {
                model.addAttribute("errorMessage", "Post not found");
                return "clubs/explore";
            }

            model.addAttribute("post", post);
            System.out.println("Viewing post: " + postId);
        } catch (Exception e) {
            System.err.println("Error loading post details: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading post details: " + e.getMessage());
        }
        return "clubs/post-details";
    }
}