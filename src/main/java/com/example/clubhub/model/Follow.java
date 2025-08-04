package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(FollowId.class)
@Data // Lombok: generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: generates a no-args constructor
public class Follow {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "club_id")
    private UUID clubId;

    private Date createdAt;
}