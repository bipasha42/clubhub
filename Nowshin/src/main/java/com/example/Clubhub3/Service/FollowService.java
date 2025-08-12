package com.example.Clubhub3.Service;

import com.example.Clubhub3.model.Club;
import com.example.Clubhub3.model.Follow;
import com.example.Clubhub3.model.User;
import com.example.Clubhub3.repo.ClubShowcaseRepository;
import com.example.Clubhub3.repo.FollowRepository;
import com.example.Clubhub3.repo.ClubAdminUniadminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FollowService {
    
    @Autowired
    private FollowRepository followRepository;
    
    @Autowired
    private ClubShowcaseRepository clubRepository;
    
    @Autowired
    private ClubAdminUniadminRepository userRepository;

    @Transactional
    public boolean toggleFollow(UUID clubId, UUID userId) {
        try {
            Club club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new RuntimeException("Club not found"));
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean isFollowing = followRepository.existsByClubIdAndUserId(clubId, userId);
            
            if (isFollowing) {
                followRepository.deleteByClubIdAndUserId(clubId, userId);
                return false;
            } else {
                Follow follow = new Follow(club, user);
                followRepository.save(follow);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            throw new RuntimeException("Failed to toggle follow status: " + e.getMessage());
        }
    }

    public boolean isFollowing(UUID clubId, UUID userId) {
        return followRepository.existsByClubIdAndUserId(clubId, userId);
    }
}
