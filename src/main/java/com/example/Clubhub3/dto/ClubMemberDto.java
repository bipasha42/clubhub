package com.example.Clubhub3.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubMemberDto {
    private UUID id;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String profilePictureUrl;
    private LocalDateTime memberSince;
    private boolean isAdmin;
}