package com.example.clubhub.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id", updatable = false, nullable = false)
    private UUID notificationId;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "isread")
    private Boolean isRead;

    @Column(name = "notification_createdat")
    private Date notificationCreatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "related_post_id")
    private Post relatedPost;

    @ManyToOne
    @JoinColumn(name = "related_event_id")
    private RSVP relatedEvent;
}