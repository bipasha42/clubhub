package com.example.clubhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clubhub.model.Follow;
import com.example.clubhub.model.FollowId;
public interface FollowRepository extends JpaRepository<Follow, FollowId> {}