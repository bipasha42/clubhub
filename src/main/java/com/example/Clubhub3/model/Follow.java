package com.example.Clubhub3.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "Follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "followed_at")
    private ZonedDateTime followedAt;

    public Follow() {}

    public Follow(Club club, User user) {
        this.club = club;
        this.user = user;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ZonedDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(ZonedDateTime followedAt) {
        this.followedAt = followedAt;
    }
}
