package com.example.clubhub.controller;


import com.example.clubhub.model.Country;
import com.example.clubhub.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin(origins = "*")
public class CountryController {
    
    private final CountryRepository countryRepository;
    
    @Autowired
    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    
    // GET all countries
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        try {
            List<Country> countries = countryRepository.findAll();
            return new ResponseEntity<>(countries, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET country by ID
    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable UUID id) {
        try {
            Optional<Country> country = countryRepository.findById(id);
            if (country.isPresent()) {
                return new ResponseEntity<>(country.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // POST create new country
    @PostMapping
    public ResponseEntity<Country> createCountry(@Validated @RequestBody Country country) {
        try {
            Country savedCountry = countryRepository.save(country);
            return new ResponseEntity<>(savedCountry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // PUT update country
    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable UUID id, 
                                               @Validated @RequestBody Country countryDetails) {
        try {
            Optional<Country> optionalCountry = countryRepository.findById(id);
            if (optionalCountry.isPresent()) {
                Country country = optionalCountry.get();
                country.setCountryName(countryDetails.getCountryName());
                Country updatedCountry = countryRepository.save(country);
                return new ResponseEntity<>(updatedCountry, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // DELETE country
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCountry(@PathVariable UUID id) {
        try {
            Optional<Country> country = countryRepository.findById(id);
            if (country.isPresent()) {
                countryRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


