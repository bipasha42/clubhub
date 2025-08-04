package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID user_id;
    private String email;
    private String passwordHash;
    private String role;
    private String user_name;
    private String bio;
    private String profilePictureUri;
    private Boolean isVerified;
    private Date user_createdAt;
    private Date user_updatedAt;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    // Constructors
    public User() {}

    // Getters and Setters
    public UUID getId() { return user_id; }
    public void setId(UUID user_id) { this.user_id = user_id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getName() { return user_name; }
    public void setName(String user_name) { this.user_name = user_name; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getProfilePictureUri() { return profilePictureUri; }
    public void setProfilePictureUri(String profilePictureUri) { this.profilePictureUri = profilePictureUri; }
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    public Date getCreatedAt() { return user_createdAt; }
    public void setCreatedAt(Date createdAt) { this.user_createdAt = createdAt; }
    public Date getUpdatedAt() { return user_updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.user_updatedAt = updatedAt; }
    public University getUniversity() { return university; }
    public void setUniversity(University university) { this.university = university; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}
