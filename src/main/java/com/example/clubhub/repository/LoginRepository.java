package com.example.clubhub.repository;

import com.example.clubhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoginRepository extends JpaRepository<User, UUID> {
    User findByEmailAndPasswordHash(String email, String passwordHash);

    // Add this method:
    Optional<User> findByEmail(String email);
}