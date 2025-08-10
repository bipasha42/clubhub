package com.example.Clubhub3.repo;
import com.example.Clubhub3.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UniversityUniadminRepository extends JpaRepository<University, UUID> {
    
    @Query("SELECT u FROM University u JOIN FETCH u.country ORDER BY u.name")
    List<University> findAllWithCountry();
    
    @Query("SELECT u FROM University u JOIN FETCH u.country WHERE u.id = :id")
    University findByIdWithCountry(UUID id);
}


