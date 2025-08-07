package com.example.clubhub.controller;

import com.example.clubhub.model.Post;
import com.example.clubhub.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/posts")
public class PostPageController {

    @Autowired
    private PostRepository postRepository;

    // List all posts
    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "posts";
    }
    // Show only approved posts
@GetMapping("/approved")
public String listApprovedPosts(Model model) {
    model.addAttribute("posts", postRepository.findAll().stream()
        .filter(Post::isApproved)
        .toList());
    return "posts";
}

    // Show create post form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "create_post";
    }

    // Handle create post
    @PostMapping
    public String createPost(@ModelAttribute Post post) {
        post.setPostCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setPostUpdatedAt(new Timestamp(System.currentTimeMillis()));
        post.setApproved(false);
        postRepository.save(post);
        return "redirect:/posts";
    }

    // Show edit post form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "edit_post";
        } else {
            return "redirect:/posts";
        }
    }

    // Handle edit post
    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable UUID id, @ModelAttribute Post updatedPost) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setImageUri(updatedPost.getImageUri());
            post.setPostUpdatedAt(new Timestamp(System.currentTimeMillis()));
            postRepository.save(post);
        }
        return "redirect:/posts";
    }

    // Handle delete post
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable UUID id) {
        postRepository.deleteById(id);
        return "redirect:/posts";
    }

    // Handle approve post
    @PostMapping("/approve/{id}")
    public String approvePost(@PathVariable UUID id) {
        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setApproved(true);
            post.setPostUpdatedAt(new Timestamp(System.currentTimeMillis()));
            postRepository.save(post);
        }
        return "redirect:/posts";
    }
}