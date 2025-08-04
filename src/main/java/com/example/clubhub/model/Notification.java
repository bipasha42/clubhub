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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notification_id;

    private String message;
    private Boolean isRead;
    private Date notification_createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "related_post_id")
    private Post relatedPost;

    @ManyToOne
    @JoinColumn(name = "related_event_id")
    private RSVP relatedEvent;

    // Constructors
    public Notification() {}

    // Getters and Setters
    public UUID getId() { return notification_id; }
    public void setId(UUID id) { this.notification_id = id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public Date getCreatedAt() { return notification_createdAt; }
    public void setCreatedAt(Date createdAt) { this.notification_createdAt = createdAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Post getRelatedPost() { return relatedPost; }
    public void setRelatedPost(Post relatedPost) { this.relatedPost = relatedPost; }
    public RSVP getRelatedEvent() { return relatedEvent; }
    public void setRelatedEvent(RSVP relatedEvent) { this.relatedEvent = relatedEvent; }
}
