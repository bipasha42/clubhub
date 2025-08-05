package com.example.clubhub.repository;

import com.example.clubhub.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    // Find all posts by club
    List<Post> findByClubId(UUID clubId);
}