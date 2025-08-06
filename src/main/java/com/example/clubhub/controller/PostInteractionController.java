package com.example.clubhub.controller;

import com.example.clubhub.model.Comment;
import com.example.clubhub.service.PostInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostInteractionController {
    @Autowired
    private PostInteractionService postInteractionService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        postInteractionService.likePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<?> unlikePost(@PathVariable UUID postId, @RequestParam UUID userId) {
        postInteractionService.unlikePost(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable UUID postId, @RequestParam UUID userId, @RequestBody String content) {
        return ResponseEntity.ok(postInteractionService.addComment(postId, userId, content));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> editComment(@PathVariable UUID commentId, @RequestParam UUID userId, @RequestBody String content) {
        return ResponseEntity.ok(postInteractionService.editComment(commentId, userId, content));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID commentId, @RequestParam UUID userId) {
        postInteractionService.deleteComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable UUID postId) {
        return ResponseEntity.ok(postInteractionService.getComments(postId));
    }

    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<Integer> getLikeCount(@PathVariable UUID postId) {
        return ResponseEntity.ok(postInteractionService.getLikeCount(postId));
    }
    @GetMapping("/{postId}/comments/count")
    public ResponseEntity<Integer> getCommentCount(@PathVariable UUID postId) {
        return ResponseEntity.ok(postInteractionService.getCommentsCount(postId));
    }
}