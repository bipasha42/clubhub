package com.example.clubhub.service;

import com.example.clubhub.model.*;
import com.example.clubhub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostInteractionService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LoginRepository userRepository;

    // Like a post
    public void likePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        if (postLikeRepository.findByPostAndUser(post, user).isEmpty()) {
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(user);
            postLikeRepository.save(like);
        }
    }

    // Unlike a post
    public void unlikePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        postLikeRepository.deleteByPostAndUser(post, user);
    }

    // Add a comment
    public Comment addComment(UUID postId, UUID userId, String content) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    // Edit a comment
    public Comment editComment(UUID commentId, UUID userId, String newContent) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!comment.getUser().getUserId().equals(userId)) throw new RuntimeException("Not allowed");
        comment.setContent(newContent);
        comment.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        return commentRepository.save(comment);
    }

    // Delete a comment
    public void deleteComment(UUID commentId, UUID userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!comment.getUser().getUserId().equals(userId)) throw new RuntimeException("Not allowed");
        commentRepository.delete(comment);
    }

    // Get all comments for a post
    public List<Comment> getComments(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPost(post);
    }

    // Get like count for a post
    public int getLikeCount(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return postLikeRepository.countByPost(post);
    }

    // Get comments count for a post
    public int getCommentsCount(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPost(post).size();
    }

    // Get a post by ID
    public Post getPost(UUID postId) {
        return postRepository.findById(postId).orElseThrow();
    }

    // Check if a user liked a post
    public boolean isLikedByUser(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        return postLikeRepository.findByPostAndUser(post, user).isPresent();
    }

    // Get all comments for a post (already exists as getComments)
    public List<Comment> getCommentsForPost(UUID postId) {
        return getComments(postId);
    }

    // Get a single comment by its ID
    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId).orElseThrow();
    }
}