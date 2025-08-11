package com.example.Clubhub3.controller;

import com.example.Clubhub3.dto.ClubShowcaseDto;
import com.example.Clubhub3.dto.PostDto;
import com.example.Clubhub3.dto.ClubMemberDto;
import com.example.Clubhub3.Service.ClubShowcaseService;
import com.example.Clubhub3.Service.PostCommentService; // Add this
import com.example.Clubhub3.dto.PostCommentDto; // Add this
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.example.Clubhub3.repo.ClubAdminUniadminRepository;
import com.example.Clubhub3.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Add this
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/clubs")
public class ClubShowcaseController {

    @Autowired
    private ClubShowcaseService clubShowcaseService;

    @Autowired
    private ClubAdminUniadminRepository userRepository;

    @Autowired
    private PostCommentService postCommentService; // Add this

    @GetMapping
    public String exploreClubs(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "12") int size,
                               Authentication authentication) {
        try {
            UUID currentUserId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                // Add code to get currentUserId from authentication if needed
            }

            Page<ClubShowcaseDto> clubPage = clubShowcaseService.getAllClubsForShowcase(
                    currentUserId,
                    PageRequest.of(page, size)
            );

            if (clubPage.isEmpty()) {
                model.addAttribute("clubs", List.of());
                model.addAttribute("infoMessage", "No clubs found.");
            } else {
                model.addAttribute("clubs", clubPage.getContent());
                System.out.println("Found " + clubPage.getTotalElements() + " clubs for showcase (page " + (page + 1) + " of " + clubPage.getTotalPages() + ")");
            }

            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", clubPage.getTotalPages());
            model.addAttribute("totalItems", clubPage.getTotalElements());

        } catch (Exception e) {
            System.err.println("Error loading clubs: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("clubs", List.of());
            model.addAttribute("errorMessage", "Error loading clubs: " + e.getMessage());
        }
        return "clubs/explore";
    }

    @GetMapping("/{clubId}")
    public String viewClub(@PathVariable UUID clubId, Model model, Authentication authentication) {
        try {
            System.out.println("=== DEBUG: Viewing club with ID: " + clubId + " ===");
            UUID currentUserId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                String userEmail = authentication.getName();
                currentUserId = userRepository.findByEmail(userEmail)
                        .map(User::getId)
                        .orElse(null);
                System.out.println("=== DEBUG: Current user ID: " + currentUserId + " ===");
            }

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
    public String viewPost(@PathVariable UUID postId, Model model, Authentication authentication) { // Updated method signature
        try {
            UUID currentUserId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                String userEmail = authentication.getName();
                currentUserId = userRepository.findByEmail(userEmail)
                        .map(User::getId)
                        .orElse(null);
            }

            PostDto post = clubShowcaseService.getPostDetails(postId, currentUserId);
            if (post == null) {
                model.addAttribute("errorMessage", "Post not found");
                return "clubs/explore";
            }

            // Get comments for the post
            List<PostCommentDto> comments = clubShowcaseService.getPostComments(postId);

            model.addAttribute("post", post);
            model.addAttribute("comments", comments); // Add this
            System.out.println("Viewing post: " + postId);
        } catch (Exception e) {
            System.err.println("Error loading post details: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading post details: " + e.getMessage());
        }
        return "clubs/post-details";
    }

    // Add comment endpoint
    @PostMapping("/posts/{postId}/comment")
    public String addComment(@PathVariable UUID postId,
                             @RequestParam String content,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            if (authentication == null) {
                return "redirect:/login";
            }

            String userEmail = authentication.getName();
            UUID userId = userRepository.findByEmail(userEmail)
                    .map(User::getId)
                    .orElse(null);

            if (userId == null) {
                return "redirect:/login";
            }

            postCommentService.addComment(postId, userId, content);
            redirectAttributes.addFlashAttribute("successMessage", "Comment added successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add comment");
        }

        return "redirect:/clubs/posts/" + postId;
    }
}