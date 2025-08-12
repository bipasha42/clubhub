package com.example.Clubhub3.repo;

import com.example.Clubhub3.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClubShowcaseRepository extends JpaRepository<Club, UUID> {

    @Query("SELECT c FROM Club c JOIN FETCH c.university JOIN FETCH c.admin")
    List<Club> findAllClubsForShowcase();

    @Query(value = "SELECT c FROM Club c JOIN FETCH c.university JOIN FETCH c.admin ORDER BY c.createdAt DESC",
           countQuery = "SELECT COUNT(c) FROM Club c")
    Page<Club> findAllClubsForShowcasePaginated(Pageable pageable);

    @Query("SELECT c FROM Club c JOIN FETCH c.university JOIN FETCH c.admin WHERE c.id = :clubId")
    Club findClubWithDetails(@Param("clubId") UUID clubId);

    @Query(value = "SELECT COUNT(*) FROM clubmember WHERE club_id = :clubId", nativeQuery = true)
    int countMembersByClubId(@Param("clubId") UUID clubId);

    @Query(value = "SELECT COUNT(*) FROM post WHERE club_id = :clubId", nativeQuery = true)
    int countPostsByClubId(@Param("clubId") UUID clubId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM follow WHERE club_id = :clubId AND user_id = :userId", nativeQuery = true)
    boolean isUserFollowingClub(@Param("clubId") UUID clubId, @Param("userId") UUID userId);
}