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