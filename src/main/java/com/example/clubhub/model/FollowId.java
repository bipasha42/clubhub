package com.example.clubhub.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class FollowId implements Serializable {
    private UUID userId;
    private UUID clubId;

    public FollowId() {}
    public FollowId(UUID userId, UUID clubId) {
        this.userId = userId;
        this.clubId = clubId;
    }

    // equals and hashCode
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
