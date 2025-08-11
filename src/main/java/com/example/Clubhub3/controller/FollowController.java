package com.example.Clubhub3.controller;

import com.example.Clubhub3.Service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.UUID;
import com.example.Clubhub3.repo.ClubAdminUniadminRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.Clubhub3.model.User;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private ClubAdminUniadminRepository userRepository; // Repository for User table

    @PostMapping("/api/clubs/follow")
    @ResponseBody
    public ResponseEntity<Boolean> toggleFollow(@RequestParam UUID clubId, Authentication authentication) {
        System.out.println("Follow request received for clubId: " + clubId);
        try {
            if (authentication == null) {
                System.out.println("No authentication found");
                return ResponseEntity.status(401).body(false);
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            System.out.println("User requesting follow: " + username);
            
            // Try to find by email directly since we're storing emails as usernames
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found. Username: " + username));
            
            UUID userId = user.getId();
            boolean isNowFollowing = followService.toggleFollow(clubId, userId);
            System.out.println("Toggle result: " + isNowFollowing);
            return ResponseEntity.ok(isNowFollowing);
        } catch (Exception e) {
            System.err.println("Error in toggleFollow: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(false);
        }
    }
}
