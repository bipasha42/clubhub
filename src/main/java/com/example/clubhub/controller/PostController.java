package com.example.clubhub.controller;

import com.example.clubhub.model.Post;
import com.example.clubhub.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Get all posts (for admin)
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Get all approved posts (for users)
    @GetMapping("/approved")
    public List<Post> getApprovedPosts() {
        return postService.getApprovedPosts();
    }

    // Get a single post by ID
    @GetMapping("/{id}")
    public Optional<Post> getPostById(@PathVariable UUID id) {
        return postService.getPostById(id);
    }

    // Create a new post
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    // Update a post
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable UUID id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable UUID id) {
        boolean deleted = postService.deletePost(id);
        return deleted ? "Post deleted" : "Post not found";
    }

    // Approve a post
    @PutMapping("/{id}/approve")
    public Post approvePost(@PathVariable UUID id) {
        return postService.approvePost(id);
    }
}
