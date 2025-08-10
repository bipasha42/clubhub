package com.example.Clubhub3.repo;

import com.example.Clubhub3.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.clubId = :clubId ORDER BY p.createdAt DESC")
    List<Post> findPostsByClubId(@Param("clubId") UUID clubId);

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :postId")
    Post findPostWithAuthor(@Param("postId") UUID postId);

    @Query(value = "SELECT COUNT(*) FROM postlikes WHERE post_id = :postId", nativeQuery = true)
    int countLikesByPostId(@Param("postId") UUID postId);

    @Query(value = "SELECT COUNT(*) FROM postcomment WHERE post_id = :postId", nativeQuery = true)
    int countCommentsByPostId(@Param("postId") UUID postId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM postlikes WHERE post_id = :postId AND user_id = :userId", nativeQuery = true)
    boolean isPostLikedByUser(@Param("postId") UUID postId, @Param("userId") UUID userId);
}