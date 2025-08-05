package com.example.clubhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "country")
@Data
@NoArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "country_id", updatable = false, nullable = false)
    private UUID countryId;

    @Column(name = "country_name", nullable = false, length = 255)
    private String countryName;

    // Custom constructor for convenience
    public Country(String countryName) {
        this.countryName = countryName;
    }
}