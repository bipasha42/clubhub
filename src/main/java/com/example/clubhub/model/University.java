package com.example.clubhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "university_id")
    private UUID universityId;

    @Column(name = "university_name")
    private String universityName;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    // Custom constructor for convenience
    public University(String universityName, Country country) {
        this.universityName = universityName;
        this.country = country;
    }
}