package com.example.Clubhub3.repo;

import com.example.Clubhub3.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM follow WHERE club_id = :clubId AND user_id = :userId", nativeQuery = true)
    boolean existsByClubIdAndUserId(@Param("clubId") UUID clubId, @Param("userId") UUID userId);

    @Modifying
    @Query(value = "DELETE FROM follow WHERE club_id = :clubId AND user_id = :userId", nativeQuery = true)
    void deleteByClubIdAndUserId(@Param("clubId") UUID clubId, @Param("userId") UUID userId);
}
