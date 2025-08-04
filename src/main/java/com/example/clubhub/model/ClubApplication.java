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
public class ClubApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID clubapplication_id;

    private String status;
    private String message;
    private Date appliedAt;
    private Date reviewedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    // Constructors
    public ClubApplication() {}

    // Getters and Setters
    public UUID getId() { return clubapplication_id; }
    public void setId(UUID id) { this.clubapplication_id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Date getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Date appliedAt) { this.appliedAt = appliedAt; }
    public Date getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Date reviewedAt) { this.reviewedAt = reviewedAt; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}
