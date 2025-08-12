package com.example.Clubhub3.repo;

import com.example.Clubhub3.model.Club;


import com.example.Clubhub3.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClubUniadminRepository extends JpaRepository<Club, UUID> {
    
    List<Club> findByUniversity(University university);
    
    List<Club> findByUniversityId(UUID universityId);
    
    boolean existsByNameAndUniversity(String name, University university);
    
    @Query("SELECT c FROM Club c JOIN FETCH c.university JOIN FETCH c.admin")
    List<Club> findAllWithUniversityAndAdmin();
    
    @Query("SELECT c FROM Club c JOIN FETCH c.university JOIN FETCH c.admin WHERE c.id = :id")
    Club findByIdWithUniversityAndAdmin(@Param("id") UUID id);
}
