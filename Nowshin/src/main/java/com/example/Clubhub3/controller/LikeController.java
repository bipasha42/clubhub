package com.example.Clubhub3.controller;

import com.example.Clubhub3.Service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.UUID;
import com.example.Clubhub3.repo.ClubAdminUniadminRepository;
import com.example.Clubhub3.model.User;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;
    private final ClubAdminUniadminRepository userRepository;

    @PostMapping("/api/posts/like")
    @ResponseBody
    public ResponseEntity<Boolean> toggleLike(@RequestParam UUID postId, Authentication authentication) {
        log.info("Like request received for postId: {}", postId);
        try {
            if (authentication == null) {
                log.warn("No authentication found for like request");
                return ResponseEntity.status(401).body(false);
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            log.info("User requesting like: {}", username);

            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found. Username: " + username));

            UUID userId = user.getId();
            boolean isNowLiked = likeService.toggleLike(postId, userId);
            log.info("Toggle result for user {} on post {}: {}", userId, postId, isNowLiked);
            return ResponseEntity.ok(isNowLiked);
        } catch (Exception e) {
            log.error("Error in toggleLike for post {}: {}", postId, e.getMessage(), e);
            return ResponseEntity.status(500).body(false);
        }
    }
}