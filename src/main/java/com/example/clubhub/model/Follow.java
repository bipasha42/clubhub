package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "follow")
@IdClass(FollowId.class)
@Data
@NoArgsConstructor
public class Follow {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Id
    @Column(name = "club_id", nullable = false)
    private UUID clubId;

    @Column(name = "createdat")
    private Date createdAt;
}