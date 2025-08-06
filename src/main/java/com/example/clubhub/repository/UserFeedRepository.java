package com.example.clubhub.repository;

import com.example.clubhub.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserFeedRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT p FROM Post p WHERE p.club.id IN " +
           "(SELECT u.club.id FROM User u WHERE u.id = :userId) OR p.club.id IN " +
           "(SELECT f.clubId FROM Follow f WHERE f.userId = :userId) OR p.club.id IN " +
           "(SELECT ca.club.id FROM ClubApplication ca WHERE ca.student.id = :userId AND ca.status = 'APPROVED')")
    List<Post> findPostsForUserDashboard(@Param("userId") UUID userId);
}