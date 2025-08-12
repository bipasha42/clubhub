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
public class PostDto {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID clubId;
    private String clubName;
    private UUID authorId;
    private String authorName;
    private int likeCount;
    private int commentCount;
    private boolean isLikedByCurrentUser;
}