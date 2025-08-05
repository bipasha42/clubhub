package com.example.clubhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Post {
    @Id
    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "imageuri") // <-- FIXED: no underscore, all lowercase
    private String imageUri;

    @Column(name = "post_createdat") // <-- FIXED: no uppercase A
    private Timestamp postCreatedAt;

    @Column(name = "post_updatedat") // <-- FIXED: no uppercase A
    private Timestamp postUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
}