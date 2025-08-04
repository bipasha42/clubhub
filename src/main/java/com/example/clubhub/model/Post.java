package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID post_id;

    private String title;
    private String content;
    private String imageUri;
    private Date post_createdAt;
    private Date post_updatedAt;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    // Constructors
    public Post() {}

    // Getters and Setters
    public UUID getId() { return post_id; }
    public void setId(UUID id) { this.post_id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public Date getCreatedAt() { return post_createdAt; }
    public void setCreatedAt(Date createdAt) { this.post_createdAt = createdAt; }
    public Date getUpdatedAt() { return post_updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.post_updatedAt = updatedAt; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}
