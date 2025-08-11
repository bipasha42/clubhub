package com.example.Clubhub3.repo;

import com.example.Clubhub3.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, UUID> {
    List<PostComment> findByPostIdOrderByCreatedAtAsc(UUID postId);
    int countByPostId(UUID postId);

    @Query("SELECT COUNT(pc) FROM PostComment pc WHERE pc.postId = :postId")
    int countCommentsByPostId(@Param("postId") UUID postId);
}