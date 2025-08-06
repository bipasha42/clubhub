package com.example.clubhub.controller;

import com.example.clubhub.model.Comment;
import com.example.clubhub.model.Post;
import com.example.clubhub.model.User;
import com.example.clubhub.service.PostInteractionService;
import com.example.clubhub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession; // Use javax.servlet.http.HttpSession if on older Spring Boot

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/posts")
public class PostThymeleafController {

    @Autowired
    private PostInteractionService postInteractionService;
    @Autowired
    private UserService userService;

    // Show the simple post page
    @GetMapping("/{postId}")
    public String showSimplePost(@PathVariable UUID postId, Model model, HttpSession session) {
        Post post = postInteractionService.getPost(postId);
        User currentUser = (User) session.getAttribute("currentUser");
        boolean likedByCurrentUser = false;

        if (currentUser != null) {
            likedByCurrentUser = postInteractionService.isLikedByUser(postId, currentUser.getUserId());
        }

        int likeCount = postInteractionService.getLikeCount(postId);
        List<Comment> comments = postInteractionService.getCommentsForPost(postId);

        model.addAttribute("post", post);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("likedByCurrentUser", likedByCurrentUser);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("comments", comments);
        return "post-simple"; // Renders post-simple.html
    }

    // Like a post (changed mapping to avoid conflict)
    @PostMapping("/{postId}/like-form")
    public String likePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        postInteractionService.likePost(postId, userId);
        return "redirect:/posts/" + postId;
    }

    // Unlike a post (changed mapping to avoid conflict)
    @PostMapping("/{postId}/unlike-form")
    public String unlikePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        postInteractionService.unlikePost(postId, userId);
        return "redirect:/posts/" + postId;
    }

    // Add a comment (changed mapping to avoid conflict)
    @PostMapping("/{postId}/comments-form")
    public String addComment(@PathVariable UUID postId, @RequestParam UUID userId, @RequestParam String content) {
        postInteractionService.addComment(postId, userId, content);
        return "redirect:/posts/" + postId;
    }

    // Edit or Delete a comment (changed mapping to avoid conflict)
    @PostMapping("/comments-form/{commentId}")
    public String editOrDeleteComment(
            @PathVariable UUID commentId,
            @RequestParam UUID userId,
            @RequestParam(required = false) String content,
            @RequestParam(value = "_method", required = false) String method
    ) {
        Comment comment = postInteractionService.getComment(commentId);
        UUID postId = comment.getPost().getPostId();

        if ("put".equalsIgnoreCase(method)) {
            postInteractionService.editComment(commentId, userId, content);
        } else if ("delete".equalsIgnoreCase(method)) {
            postInteractionService.deleteComment(commentId, userId);
        }
        return "redirect:/posts/" + postId;
    }
}