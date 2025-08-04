package com.example.clubhub.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clubhub.model.User;
public interface UserRepository extends JpaRepository<User, UUID> {}