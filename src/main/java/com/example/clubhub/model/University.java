package com.example.clubhub.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID university_id;
    private String university_name;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    // Constructors
    public University() {}
    public University(String university_name, Country country) {
        this.university_name = university_name;
        this.country = country;
    }
    // Getters and Setters
    public UUID getId() { return university_id; }
    public void setId(UUID university_id) { this.university_id = university_id; }
    public String getName() { return university_name; }
    public void setName(String university_name) { this.university_name = university_name; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
}
