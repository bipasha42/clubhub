package com.example.clubhub.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID country_id;
    private String country_name;
    // Constructors
    public Country() {}
    public Country(String country_name) { this.country_name = country_name; }

    // Getters and Setters
    public UUID getId() { return country_id; }
    public void setId(UUID country_id) { this.country_id = country_id; }
    public String getName() { return country_name; }
    public void setName(String country_name) { this.country_name = country_name; }
}
