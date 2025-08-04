package com.example.clubhub.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clubhub.model.ClubApplication;
public interface ClubApplicationRepository extends JpaRepository<ClubApplication, UUID> {}