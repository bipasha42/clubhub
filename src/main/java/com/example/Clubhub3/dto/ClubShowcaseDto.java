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