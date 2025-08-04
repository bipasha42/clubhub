package com.example.clubhub.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.clubhub.model.Club;


public interface ClubRepository extends JpaRepository<Club, UUID> {
    List<Club> findByNameContainingIgnoreCase(String name);
}