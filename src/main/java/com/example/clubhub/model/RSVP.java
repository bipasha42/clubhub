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
public class RSVP {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID rsvp_id;

    private String status;
    private Date rsvp_createdAt;
    private Date rsvp_updatedAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors
    public RSVP() {}

    // Getters and Setters
    public UUID getId() { return rsvp_id; }
    public void setId(UUID id) { this.rsvp_id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return rsvp_createdAt; }
    public void setCreatedAt(Date createdAt) { this.rsvp_createdAt = createdAt; }
    public Date getUpdatedAt() { return rsvp_updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.rsvp_updatedAt = updatedAt; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
