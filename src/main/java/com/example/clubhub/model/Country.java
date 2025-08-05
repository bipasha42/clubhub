package com.example.clubhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "country_id")
    private UUID countryId;

    @Column(name = "country_name")
    private String countryName;

    // Custom constructor for convenience
    public Country(String countryName) {
        this.countryName = countryName;
    }
}