package com.example.clubhub.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Lombok: generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: generates a no-args constructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID country_id;

    private String country_name;

    // Custom constructor for convenience
    public Country(String country_name) {
        this.country_name = country_name;
    }
}