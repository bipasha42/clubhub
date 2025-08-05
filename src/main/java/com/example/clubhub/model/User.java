package com.example.clubhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email")
    private String email;

    @Column(name = "passwordhash")
    private String passwordHash;

    @Column(name = "role")
    private String role;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profilepictureuri")
    private String profilePictureUri;

    @Column(name = "isverified")
    private Boolean isVerified;

    @Column(name = "user_createdat")
    private Timestamp userCreatedAt;

    @Column(name = "user_updatedat")
    private Timestamp userUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
}