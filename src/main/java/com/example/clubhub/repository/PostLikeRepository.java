package com.example.clubhub.repository;

import com.example.clubhub.model.PostLike;
import com.example.clubhub.model.Post;
import com.example.clubhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
    int countByPost(Post post);
    void deleteByPostAndUser(Post post, User user);
}
