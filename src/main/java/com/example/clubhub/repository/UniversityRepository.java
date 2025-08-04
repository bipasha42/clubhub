package com.example.clubhub.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clubhub.model.University;
public interface UniversityRepository extends JpaRepository<University, UUID> {}