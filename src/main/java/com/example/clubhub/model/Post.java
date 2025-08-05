package com.example.clubhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id", updatable = false, nullable = false)
    private UUID postId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "imageuri", length = 255)
    private String imageUri;

    @Column(name = "post_createdat")
    private Timestamp postCreatedAt;

    @Column(name = "post_updatedat")
    private Timestamp postUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
}