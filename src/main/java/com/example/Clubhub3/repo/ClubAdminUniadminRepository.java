package com.example.Clubhub3.repo;

import com.example.Clubhub3.model.User;
import com.example.Clubhub3.model.UserRole;

import com.example.Clubhub3.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClubAdminUniadminRepository extends JpaRepository<User, UUID> {
    
    List<User> findByRoleAndUniversity(UserRole role, University university);
    
    List<User> findByRole(UserRole role);
    
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :username")
    Optional<User> findByUsername(@Param("username") String username);
    
    @Query("SELECT u FROM User u JOIN FETCH u.university WHERE u.role = :role")
    List<User> findByRoleWithUniversity(@Param("role") UserRole role);
    
    @Query("SELECT u FROM User u JOIN FETCH u.university WHERE u.id = :id")
    User findByIdWithUniversity(@Param("id") UUID id);
}

