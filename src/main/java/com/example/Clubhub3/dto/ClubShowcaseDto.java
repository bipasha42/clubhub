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
public class ClubShowcaseDto {
    private UUID id;
    private String name;
    private String description;
    private String logoUrl;
    private String bannerUrl;
    private LocalDateTime createdAt;
    private String universityName;
    private String adminName;
    private int memberCount;
    private int postCount;
    private boolean isFollowing;
}