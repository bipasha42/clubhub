package com.example.clubhub.repository;

import com.example.clubhub.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClubRepository extends JpaRepository<Club, UUID> {
    // Search clubs by name (case-insensitive, partial match)
    List<Club> findByNameContainingIgnoreCase(String name);
}