package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Lombok: generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: generates a no-args constructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID post_id;

    private String title;
    private String content;
    private String imageUri;
    private Date post_createdAt;
    private Date post_updatedAt;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;
}