package com.example.clubhub.service;

import com.example.clubhub.model.Post;
import com.example.clubhub.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getApprovedPosts() {
        return postRepository.findAll().stream()
                .filter(Post::isApproved)
                .toList();
    }

    public Optional<Post> getPostById(UUID postId) {
        return postRepository.findById(postId);
    }

    public Post createPost(Post post) {
        post.setPostCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setPostUpdatedAt(new Timestamp(System.currentTimeMillis()));
        post.setApproved(false); // New posts are not approved by default
        return postRepository.save(post);
    }

    public Post updatePost(UUID postId, Post updatedPost) {
        return postRepository.findById(postId).map(post -> {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setImageUri(updatedPost.getImageUri());
            post.setPostUpdatedAt(new Timestamp(System.currentTimeMillis()));
            // Do not change approval status here
            return postRepository.save(post);
        }).orElse(null);
    }

    public boolean deletePost(UUID postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }

    public Post approvePost(UUID postId) {
        return postRepository.findById(postId).map(post -> {
            post.setApproved(true);
            post.setPostUpdatedAt(new Timestamp(System.currentTimeMillis()));
            return postRepository.save(post);
        }).orElse(null);
    }
}