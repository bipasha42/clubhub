package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clubapplication")
@Data
@NoArgsConstructor
public class ClubApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "clubapplication_id", updatable = false, nullable = false)
    private UUID clubapplication_id;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "appliedat")
    private Date appliedAt;

    @Column(name = "reviewedat")
    private Date reviewedAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
}