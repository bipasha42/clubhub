package com.example.Clubhub3.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubMemberDto {
    private UUID id;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String profilePictureUrl;
    private LocalDateTime memberSince;
    private boolean isAdmin;
}