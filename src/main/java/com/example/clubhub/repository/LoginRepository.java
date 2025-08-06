package com.example.clubhub.repository;

import com.example.clubhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {
    User findByEmailAndPasswordHash(String email, String passwordHash);
}
