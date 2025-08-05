package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rsvp")
@Data
@NoArgsConstructor
public class RSVP {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "rsvp_id", updatable = false, nullable = false)
    private UUID rsvpId;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "rsvp_createdat")
    private Date rsvpCreatedAt;

    @Column(name = "rsvp_updatedat")
    private Date rsvpUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}