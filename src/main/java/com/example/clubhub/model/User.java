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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "passwordhash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "user_name", length = 255)
    private String userName;

    @Column(name = "bio", columnDefinition = "text")
    private String bio;

    @Column(name = "profilepictureuri", length = 255)
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

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
}
