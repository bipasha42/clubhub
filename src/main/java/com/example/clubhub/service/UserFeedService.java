package com.example.clubhub.service;

import com.example.clubhub.model.Post;
import com.example.clubhub.repository.UserFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserFeedService {

    private final UserFeedRepository userFeedRepository;

    @Autowired
    public UserFeedService(UserFeedRepository userFeedRepository) {
        this.userFeedRepository = userFeedRepository;
    }

    public List<Post> getDashboardPosts(UUID userId) {
        return userFeedRepository.findPostsForUserDashboard(userId);
    }
}
