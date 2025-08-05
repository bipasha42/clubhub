package com.example.clubhub.repository;

import com.example.clubhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Find all users by club
    List<User> findByClubId(UUID clubId);
}