package com.example.Clubhub3.Service;

import com.example.Clubhub3.model.Post;
import com.example.Clubhub3.model.User;
import com.example.Clubhub3.repo.PostRepository;
import com.example.Clubhub3.repo.ClubAdminUniadminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final PostRepository postRepository;
    private final ClubAdminUniadminRepository userRepository;

    @Transactional
    public boolean toggleLike(UUID postId, UUID userId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean isLiked = postRepository.isPostLikedByUser(postId, userId);

            if (isLiked) {
                // Unlike the post
                postRepository.removeLike(postId, userId);
                log.info("User {} unliked post {}", userId, postId);
                return false;
            } else {
                // Like the post
                postRepository.addLike(postId, userId);
                log.info("User {} liked post {}", userId, postId);
                return true;
            }
        } catch (Exception e) {
            log.error("Failed to toggle like status for post {}: {}", postId, e.getMessage(), e);
            throw new RuntimeException("Failed to toggle like status: " + e.getMessage());
        }
    }

    public boolean isLiked(UUID postId, UUID userId) {
        return postRepository.isPostLikedByUser(postId, userId);
    }
}