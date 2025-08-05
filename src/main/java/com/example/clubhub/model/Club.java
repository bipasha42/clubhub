package com.example.clubhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Club {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "contactemail")
    private String contactEmail;

    @Column(name = "isapproved")
    private Boolean isApproved;

    @Column(name = "createdat")
    private Timestamp createdAt;

    @Column(name = "updatedat")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;
}