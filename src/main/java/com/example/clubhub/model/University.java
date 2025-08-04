package com.example.clubhub.model;

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
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID university_id;

    private String university_name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    // Custom constructor for convenience
    public University(String university_name, Country country) {
        this.university_name = university_name;
        this.country = country;
    }
}