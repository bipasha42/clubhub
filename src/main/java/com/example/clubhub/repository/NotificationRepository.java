package com.example.clubhub.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clubhub.model.Notification;
public interface NotificationRepository extends JpaRepository<Notification, UUID> {}