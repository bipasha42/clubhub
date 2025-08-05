package com.example.clubhub.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class FollowId implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID userId;
    private UUID clubId;

    public FollowId() {}

    public FollowId(UUID userId, UUID clubId) {
        this.userId = userId;
        this.clubId = clubId;
    }

    // Getters and setters (recommended for JPA)
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getClubId() {
        return clubId;
    }

    public void setClubId(UUID clubId) {
        this.clubId = clubId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowId)) return false;
        FollowId that = (FollowId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(clubId, that.clubId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clubId);
    }
}